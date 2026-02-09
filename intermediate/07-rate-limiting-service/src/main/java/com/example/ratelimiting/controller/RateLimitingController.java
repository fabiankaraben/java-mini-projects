package com.example.ratelimiting.controller;

import com.example.ratelimiting.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitingController {

    @Autowired
    private RateLimiterService rateLimiterService;

    // Rate limit: 10 requests per second, burst capacity of 20
    private static final double RATE = 10.0;
    private static final double CAPACITY = 20.0;

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {
        String key = "rate_limit:" + userId;
        
        if (rateLimiterService.allowRequest(key, RATE, CAPACITY, 1)) {
            return ResponseEntity.ok("Request allowed");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
        }
    }
}
