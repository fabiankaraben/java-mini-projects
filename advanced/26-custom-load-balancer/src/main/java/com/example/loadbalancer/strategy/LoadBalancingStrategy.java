package com.example.loadbalancer.strategy;

import java.util.List;

public interface LoadBalancingStrategy {
    String getNextBackend(List<String> backendUrls);
}
