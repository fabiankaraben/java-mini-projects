package com.example.discovery;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ServiceRegistry {

    // Key: serviceId + ":" + host + ":" + port
    private final Map<String, ServiceInstance> registry = new ConcurrentHashMap<>();

    // Threshold in seconds to consider an instance expired
    private static final long EXPIRATION_THRESHOLD_SECONDS = 30;

    public void register(ServiceInstance instance) {
        String key = generateKey(instance);
        instance.renew();
        registry.put(key, instance);
    }

    public void renew(String serviceId, String host, int port) {
        String key = serviceId + ":" + host + ":" + port;
        ServiceInstance instance = registry.get(key);
        if (instance != null) {
            instance.renew();
        } else {
            // If not found, create a new one (or throw exception depending on policy, here we re-register)
            register(new ServiceInstance(serviceId, host, port));
        }
    }

    public List<ServiceInstance> getAllInstances() {
        return new ArrayList<>(registry.values());
    }

    public List<ServiceInstance> getInstances(String serviceId) {
        return registry.values().stream()
                .filter(i -> i.getServiceId().equals(serviceId))
                .collect(Collectors.toList());
    }

    public void removeStaleInstances() {
        Instant now = Instant.now();
        registry.entrySet().removeIf(entry -> {
            ServiceInstance instance = entry.getValue();
            boolean isStale = instance.getLastHeartbeat().plusSeconds(EXPIRATION_THRESHOLD_SECONDS).isBefore(now);
            if (isStale) {
                System.out.println("Evicting stale instance: " + entry.getKey());
            }
            return isStale;
        });
    }

    public void unregister(String serviceId, String host, int port) {
        String key = serviceId + ":" + host + ":" + port;
        registry.remove(key);
    }

    public void clear() {
        registry.clear();
    }

    private String generateKey(ServiceInstance instance) {
        return instance.getServiceId() + ":" + instance.getHost() + ":" + instance.getPort();
    }
}
