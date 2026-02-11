package com.example.webhook;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebhookIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @Test
    void testWebhookDelivery() {
        // 1. Setup WireMock receiver
        String endpoint = "/webhook-receiver";
        String callbackUrl = "http://localhost:" + wireMockServer.port() + endpoint;
        
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse().withStatus(200)));

        // 2. Register Webhook
        Map<String, String> registration = Map.of(
                "url", callbackUrl,
                "eventType", "ORDER_CREATED"
        );
        
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
                "/api/webhooks/register", registration, Map.class);
        
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // 3. Trigger Event
        Map<String, Object> event = Map.of(
                "eventType", "ORDER_CREATED",
                "payload", Map.of("orderId", "12345", "amount", 99.99)
        );

        ResponseEntity<Void> triggerResponse = restTemplate.postForEntity(
                "/api/webhooks/trigger", event, Void.class);

        assertThat(triggerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        // 4. Verify Delivery (Async)
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> 
            verify(postRequestedFor(urlEqualTo(endpoint))
                    .withRequestBody(containing("12345"))
                    .withHeader("Content-Type", containing("application/json")))
        );
    }
}
