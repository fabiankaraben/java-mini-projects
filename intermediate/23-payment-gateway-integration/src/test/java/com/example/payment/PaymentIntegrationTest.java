package com.example.payment;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PaymentIntegrationTest {

    @Container
    public static GenericContainer<?> stripeMock = new GenericContainer<>(DockerImageName.parse("stripemock/stripe-mock:latest"))
            .withExposedPorts(12111, 12112);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("stripe.api.base", () -> "http://" + stripeMock.getHost() + ":" + stripeMock.getMappedPort(12111));
    }

    @BeforeAll
    static void setup() {
        // Ensure stripe-mock is ready
        assertThat(stripeMock.isRunning()).isTrue();
    }

    @Test
    void testCreatePaymentIntent() {
        String url = "http://localhost:" + port + "/api/payment/create-payment-intent";
        
        Map<String, Object> request = new HashMap<>();
        request.put("amount", 1000);
        request.put("currency", "usd");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsKey("clientSecret");
        String clientSecret = (String) response.getBody().get("clientSecret");
        assertThat(clientSecret).startsWith("pi_");
    }

    @Test
    void testWebhookHandling() throws Exception {
        String url = "http://localhost:" + port + "/api/webhook";
        
        // Construct a sample payload
        String payload = "{\n" +
                "  \"id\": \"evt_test_webhook\",\n" +
                "  \"object\": \"event\",\n" +
                "  \"type\": \"payment_intent.succeeded\",\n" +
                "  \"data\": {\n" +
                "    \"object\": {\n" +
                "      \"id\": \"pi_test_123\",\n" +
                "      \"object\": \"payment_intent\",\n" +
                "      \"amount\": 1000,\n" +
                "      \"currency\": \"usd\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        long timestamp = System.currentTimeMillis() / 1000;
        
        String sigHeader = generateStripeSignature(payload, webhookSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Stripe-Signature", sigHeader);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Received");
    }

    private String generateStripeSignature(String payload, String secret) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String payloadToSign = timestamp + "." + payload;
        
        javax.crypto.Mac sha256_HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        
        byte[] hash = sha256_HMAC.doFinal(payloadToSign.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        
        return "t=" + timestamp + ",v1=" + hexString.toString();
    }
}
