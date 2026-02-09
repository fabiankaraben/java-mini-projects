package com.fabiankaraben.circuitbreaker.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    private static final String SERVICE_NAME = "externalService";

    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallback")
    public String callExternalApi() {
        logger.info("Calling external API...");
        // This URL will be mocked in tests or can be a real URL
        return restTemplate.getForObject("http://localhost:8081/api/external", String.class);
    }

    public String fallback(Exception e) {
        logger.warn("Circuit Breaker fallback triggered due to: {}", e.getMessage());
        return "Fallback response: External service is currently unavailable.";
    }
}
