package com.fabiankaraben.miniprojects.servicea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceAController {

    private static final Logger log = LoggerFactory.getLogger(ServiceAController.class);
    private final RestTemplate restTemplate;

    @Value("${service.b.url:http://localhost:8082}")
    private String serviceBUrl;

    public ServiceAController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("Service A: Received hello request. Calling Service B...");
        String response = restTemplate.getForObject(serviceBUrl + "/greet", String.class);
        log.info("Service A: Received response from Service B: {}", response);
        return "Service A calling -> " + response;
    }
}
