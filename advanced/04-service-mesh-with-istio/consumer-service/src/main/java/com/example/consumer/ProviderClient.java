package com.example.consumer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
public class ProviderClient {

    private final RestTemplate restTemplate;
    private final String providerUrl;

    public ProviderClient(RestTemplate restTemplate, @Value("${provider.url:http://localhost:8081}") String providerUrl) {
        this.restTemplate = restTemplate;
        this.providerUrl = providerUrl;
    }

    public String getGreeting() {
        Map response = restTemplate.getForObject(providerUrl + "/greeting", Map.class);
        return (String) response.get("message");
    }
}
