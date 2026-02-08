package com.example.metrics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MetricsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldExposeMetricsAfterRequests() {
        // 1. Hit the endpoints to generate metrics
        restTemplate.getForEntity("http://localhost:" + port + "/hello", String.class);
        restTemplate.getForEntity("http://localhost:" + port + "/hello", String.class);
        restTemplate.getForEntity("http://localhost:" + port + "/random", String.class);

        // 2. Fetch the metrics
        // Based on our application.properties, it should be at /metrics
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/metrics", String.class);
        
        assertThat(response.getStatusCode().is2xxSuccessful())
                .withFailMessage("Expected 2xx but got %s with body: %s", response.getStatusCode(), response.getBody())
                .isTrue();
        String body = response.getBody();
        assertThat(body).isNotNull();

        // 3. Verify specific metric keys exist
        // Spring Boot Actuator with Micrometer + Prometheus exposes 'http_server_requests_seconds' or similar by default
        // And we added @Timed 'hello.request'
        
        // Note: Metric names in Prometheus format usually replace dots with underscores
        assertThat(body).contains("hello_request_seconds_count");
        assertThat(body).contains("hello_request_seconds_sum");
        
        // Also check for default JVM metrics
        assertThat(body).contains("jvm_memory_used_bytes");
    }
}
