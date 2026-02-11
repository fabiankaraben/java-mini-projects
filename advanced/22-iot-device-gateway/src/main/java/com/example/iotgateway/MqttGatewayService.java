package com.example.iotgateway;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MqttGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(MqttGatewayService.class);

    @Value("${mqtt.broker.host:localhost}")
    private String brokerHost;

    @Value("${mqtt.broker.port:1883}")
    private int brokerPort;

    private Mqtt5AsyncClient client;
    private final Map<String, String> lastDeviceMessage = new ConcurrentHashMap<>();

    @PostConstruct
    public void connect() {
        String identifier = UUID.randomUUID().toString();
        client = MqttClient.builder()
                .useMqttVersion5()
                .identifier(identifier)
                .serverHost(brokerHost)
                .serverPort(brokerPort)
                .buildAsync();

        client.connectWith()
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to connect to MQTT broker", throwable);
                    } else {
                        logger.info("Connected to MQTT broker: {}:{}", brokerHost, brokerPort);
                        subscribeToDevices();
                    }
                });
    }

    private void subscribeToDevices() {
        client.subscribeWith()
                .topicFilter("devices/+/data")
                .callback(publish -> {
                    String topic = publish.getTopic().toString();
                    String payload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    logger.info("Received message on topic {}: {}", topic, payload);
                    
                    // Extract device ID from topic: devices/{deviceId}/data
                    String[] parts = topic.split("/");
                    if (parts.length >= 2) {
                        String deviceId = parts[1];
                        lastDeviceMessage.put(deviceId, payload);
                    }
                })
                .send()
                .whenComplete((subAck, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to subscribe to topics", throwable);
                    } else {
                        logger.info("Subscribed to devices/+/data");
                    }
                });
    }

    public CompletableFuture<Void> sendCommand(String deviceId, String command) {
        String topic = "devices/" + deviceId + "/command";
        return client.publishWith()
                .topic(topic)
                .payload(command.getBytes(StandardCharsets.UTF_8))
                .send()
                .thenAccept(publishResult -> logger.info("Sent command to {}: {}", topic, command));
    }

    public String getLastMessage(String deviceId) {
        return lastDeviceMessage.get(deviceId);
    }
    
    public Map<String, String> getAllDeviceMessages() {
        return lastDeviceMessage;
    }

    @PreDestroy
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
}
