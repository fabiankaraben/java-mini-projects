package com.example.ratelimiter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Boolean> rateLimitScript;

    @Autowired
    public RateLimiterService(RedisTemplate<String, Object> redisTemplate, RedisScript<Boolean> rateLimitScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = rateLimitScript;
    }

    /**
     * Checks if a request is allowed based on the Sliding Window Log algorithm.
     *
     * @param key           Unique identifier for the client/resource (e.g., IP address, user ID)
     * @param maxRequests   Maximum number of requests allowed in the window
     * @param windowSizeMs  Window size in milliseconds
     * @return true if allowed, false if rate limited
     */
    public boolean isAllowed(String key, int maxRequests, long windowSizeMs) {
        String redisKey = "rate_limit:" + key;
        long currentTime = System.currentTimeMillis();

        Boolean allowed = redisTemplate.execute(
                rateLimitScript,
                Collections.singletonList(redisKey),
                String.valueOf(currentTime),
                String.valueOf(windowSizeMs),
                String.valueOf(maxRequests)
        );

        return allowed != null && allowed;
    }
}
