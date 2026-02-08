package com.example.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedComputationService implements ComputationService {

    private final ComputationService delegate;
    private final Map<String, String> cache;

    public CachedComputationService(ComputationService delegate) {
        this.delegate = delegate;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public String compute(String input) {
        return cache.computeIfAbsent(input, key -> {
            System.out.println("Cache miss for key: " + key);
            return delegate.compute(key);
        });
    }

    public int getCacheSize() {
        return cache.size();
    }
    
    public void clearCache() {
        cache.clear();
    }
}
