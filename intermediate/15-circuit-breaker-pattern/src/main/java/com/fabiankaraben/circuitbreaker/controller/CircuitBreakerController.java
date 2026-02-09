package com.fabiankaraben.circuitbreaker.controller;

import com.fabiankaraben.circuitbreaker.service.ExternalApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CircuitBreakerController {

    private final ExternalApiService externalApiService;

    public CircuitBreakerController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/data")
    public String getData() {
        return externalApiService.callExternalApi();
    }
}
