package com.example.ratelimiting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class RateLimiterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<List> rateLimiterScript;

    // Rate: tokens per second
    // Capacity: max burst
    public boolean allowRequest(String key, double rate, double capacity, int requested) {
        List<String> keys = Arrays.asList(key + ".tokens", key + ".timestamp");
        long now = Instant.now().getEpochSecond();
        
        List results = redisTemplate.execute(rateLimiterScript, keys, String.valueOf(rate), String.valueOf(capacity), String.valueOf(now), String.valueOf(requested));
        
        if (results != null && !results.isEmpty()) {
            Long allowed = (Long) results.get(0);
            return allowed == 1L;
        }
        return false;
    }
}
