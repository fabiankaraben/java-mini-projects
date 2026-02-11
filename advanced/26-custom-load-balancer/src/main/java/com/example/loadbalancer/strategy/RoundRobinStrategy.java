package com.example.loadbalancer.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancingStrategy {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String getNextBackend(List<String> backendUrls) {
        if (backendUrls == null || backendUrls.isEmpty()) {
            throw new IllegalArgumentException("Backend list cannot be empty");
        }
        int index = counter.getAndIncrement() % backendUrls.size();
        return backendUrls.get(Math.abs(index));
    }
}
