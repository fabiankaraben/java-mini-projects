package com.example.distributedcounter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private final StringRedisTemplate redisTemplate;

    public CounterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Adds an element to the HyperLogLog structure.
     * @param key The key of the counter (e.g., "page_views").
     * @param element The unique element to add (e.g., user ID, IP address).
     */
    public void add(String key, String... element) {
        redisTemplate.opsForHyperLogLog().add(key, element);
    }

    /**
     * Gets the approximate count of unique elements in the HyperLogLog structure.
     * @param key The key of the counter.
     * @return The approximate count.
     */
    public Long count(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }
    
    /**
     * Merges multiple HyperLogLog keys into a destination key.
     * @param destination The destination key.
     * @param sourceKeys The source keys to merge.
     */
    public void merge(String destination, String... sourceKeys) {
        redisTemplate.opsForHyperLogLog().union(destination, sourceKeys);
    }
    
    /**
     * Delete the counter key.
     * @param key The key to delete.
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
