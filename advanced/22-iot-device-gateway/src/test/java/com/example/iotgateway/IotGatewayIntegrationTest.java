package com.example.iotgateway;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class IotGatewayIntegrationTest {

    @Container
    static final GenericContainer<?> mosquitto = new GenericContainer<>(DockerImageName.parse("eclipse-mosquitto:2.0"))
            .withExposedPorts(1883)
            .withCommand("mosquitto -c /mosquitto-no-auth.conf")
            .withCopyFileToContainer(
                    org.testcontainers.images.builder.Transferable.of("allow_anonymous true\nlistener 1883"),
                    "/mosquitto-no-auth.conf");

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void mqttProperties(DynamicPropertyRegistry registry) {
        registry.add("mqtt.broker.host", mosquitto::getHost);
        registry.add("mqtt.broker.port", () -> mosquitto.getMappedPort(1883));
    }

    @Test
    void testDeviceDataFlow() throws Exception {
        // 1. Simulate a device sending data
        String deviceId = "device-123";
        String payload = "{\"temp\": 25.5}";
        
        publishMessage("devices/" + deviceId + "/data", payload);

        // 2. Verify via REST API that the data was received
        // Allow some time for async processing
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            ResponseEntity<String> response = restTemplate.getForEntity("/api/devices/" + deviceId + "/data", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(payload);
        });
    }

    @Test
    void testSendCommand() throws Exception {
        String deviceId = "device-456";
        String command = "{\"action\": \"reboot\"}";
        String commandTopic = "devices/" + deviceId + "/command";

        // Create a test client to subscribe to commands
        Mqtt5BlockingClient testClient = MqttClient.builder()
                .useMqttVersion5()
                .identifier("test-listener-" + UUID.randomUUID())
                .serverHost(mosquitto.getHost())
                .serverPort(mosquitto.getMappedPort(1883))
                .buildBlocking();

        testClient.connect();
        
        // Subscribe and use a container to hold received message
        final StringBuilder receivedPayload = new StringBuilder();
        testClient.toAsync().subscribeWith()
                .topicFilter(commandTopic)
                .callback(publish -> receivedPayload.append(new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8)))
                .send()
                .join();

        // 3. Send command via REST API
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/devices/" + deviceId + "/command", 
                command, 
                String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 4. Verify message received on MQTT
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(receivedPayload.toString()).isEqualTo(command);
        });

        testClient.disconnect();
    }

    private void publishMessage(String topic, String payload) {
        Mqtt5BlockingClient publisher = MqttClient.builder()
                .useMqttVersion5()
                .identifier("test-publisher-" + UUID.randomUUID())
                .serverHost(mosquitto.getHost())
                .serverPort(mosquitto.getMappedPort(1883))
                .buildBlocking();

        publisher.connect();
        publisher.publishWith()
                .topic(topic)
                .payload(payload.getBytes(StandardCharsets.UTF_8))
                .send();
        publisher.disconnect();
    }
}
