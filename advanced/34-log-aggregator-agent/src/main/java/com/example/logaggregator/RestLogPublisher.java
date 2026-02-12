package com.example.logaggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestLogPublisher implements LogPublisher {

    private static final Logger logger = LoggerFactory.getLogger(RestLogPublisher.class);
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public RestLogPublisher(@Value("${log.aggregator.server.url}") String serverUrl) {
        this.restTemplate = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    @Override
    public void publish(String logLine) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            Map<String, String> body = new HashMap<>();
            body.put("log", logLine);
            body.put("timestamp", String.valueOf(System.currentTimeMillis()));

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForObject(serverUrl, entity, String.class);
            // logger.debug("Published log: {}", logLine);
        } catch (Exception e) {
            logger.error("Failed to publish log line: {}", logLine, e);
        }
    }
}
