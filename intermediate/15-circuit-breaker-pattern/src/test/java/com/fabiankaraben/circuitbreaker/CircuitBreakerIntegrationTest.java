package com.fabiankaraben.circuitbreaker;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
class CircuitBreakerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCircuitBreakerFallback() {
        // 1. Simulate a successful call
        stubFor(get(urlEqualTo("/api/external"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("External API Response")));

        ResponseEntity<String> response = restTemplate.getForEntity("/api/data", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("External API Response");

        // 2. Simulate failure to trigger Circuit Breaker
        stubFor(get(urlEqualTo("/api/external"))
                .willReturn(aResponse()
                        .withStatus(500)));

        // Call enough times to trip the circuit breaker (min calls 3, failure threshold 50%)
        // We configured slidingWindowSize=5, minimumNumberOfCalls=3
        
        // Let's force failures
        IntStream.range(0, 5).forEach(i -> {
            restTemplate.getForEntity("/api/data", String.class);
        });

        // 3. Verify Fallback is returned when Circuit is OPEN or call fails
        ResponseEntity<String> fallbackResponse = restTemplate.getForEntity("/api/data", String.class);
        assertThat(fallbackResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fallbackResponse.getBody()).contains("Fallback response");
        
        // 4. Verify that the circuit breaker is actually preventing calls to the external service
        // (Resilience4j will stop calling the external service when open)
        // We can check this by resetting the stub to success and seeing if we still get fallback
        stubFor(get(urlEqualTo("/api/external"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Recovered")));
        
        // Should still be fallback if open
        ResponseEntity<String> openCircuitResponse = restTemplate.getForEntity("/api/data", String.class);
        // Note: exact behavior depends on if it's strictly OPEN or if we hit the failure again.
        // If it's OPEN, it returns fallback immediately without calling external.
        assertThat(openCircuitResponse.getBody()).contains("Fallback response");
    }
}
