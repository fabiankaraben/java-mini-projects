package com.example.discovery;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RegistryCleaner {

    private final ServiceRegistry serviceRegistry;

    public RegistryCleaner(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void cleanRegistry() {
        serviceRegistry.removeStaleInstances();
    }
}
