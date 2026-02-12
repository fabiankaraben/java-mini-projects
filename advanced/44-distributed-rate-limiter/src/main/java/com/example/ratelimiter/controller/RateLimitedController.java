package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitedController {

    private final RateLimiterService rateLimiterService;

    @Autowired
    public RateLimitedController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/api/resource")
    public ResponseEntity<String> getResource(
            @RequestHeader(value = "X-Client-Id", defaultValue = "default") String clientId,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "10000") long windowMs) {

        boolean allowed = rateLimiterService.isAllowed(clientId, limit, windowMs);

        if (allowed) {
            return ResponseEntity.ok("Request allowed at " + System.currentTimeMillis());
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded. Try again later.");
        }
    }
}
