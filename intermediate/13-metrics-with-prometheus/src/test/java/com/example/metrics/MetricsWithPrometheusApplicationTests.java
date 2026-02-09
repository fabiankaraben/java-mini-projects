package com.example.metrics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MetricsWithPrometheusApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldExposePrometheusEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/prometheus", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("jvm_memory_used_bytes");
    }

    @Test
    void shouldIncrementCustomMetric() {
        // First trigger the custom metric
        ResponseEntity<String> helloResponse = restTemplate.getForEntity("/hello", String.class);
        assertThat(helloResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(helloResponse.getBody()).isEqualTo("Hello from Prometheus Metrics App!");

        // Then check if it appears in Prometheus metrics
        ResponseEntity<String> prometheusResponse = restTemplate.getForEntity("/actuator/prometheus", String.class);
        assertThat(prometheusResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(prometheusResponse.getBody()).contains("custom_api_requests_total");
    }
}
