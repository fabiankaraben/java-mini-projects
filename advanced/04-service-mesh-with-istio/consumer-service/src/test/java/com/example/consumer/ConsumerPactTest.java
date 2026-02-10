package com.example.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.dius.pact.core.model.PactSpecVersion;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "provider-service", pactVersion = PactSpecVersion.V3)
public class ConsumerPactTest {

    @Pact(consumer = "consumer-service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("provider is available")
            .uponReceiving("a request for greeting")
            .path("/greeting")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(Map.of("Content-Type", "application/json"))
            .body("{\"message\": \"Hello from Provider Service!\"}")
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    public void testGreeting(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();
        ProviderClient providerClient = new ProviderClient(restTemplate, mockServer.getUrl());
        String greeting = providerClient.getGreeting();
        assertEquals("Hello from Provider Service!", greeting);
    }
}
