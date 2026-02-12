package com.example.kvstore.service;

import com.example.kvstore.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyValueService {

    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final AppConfig appConfig;
    private ConsistentHashRouter router;
    private final RestTemplate restTemplate = new RestTemplate();

    public KeyValueService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void init() {
        // 10 virtual nodes per physical node for better distribution
        this.router = new ConsistentHashRouter(appConfig.getNodes(), 10);
    }

    public String get(String key) {
        // In a fully distributed system, we might check if we are the owner.
        // Or we might act as a coordinator.
        // For read, we can try local first, if not found, check who should have it.
        // But to verify replication, we should check who is RESPONSIBLE for it.
        
        List<String> preferenceList = router.getPreferenceList(key, appConfig.getReplicationFactor());
        
        // If we are in the preference list, return what we have (even if null - eventual consistency)
        if (preferenceList.contains(appConfig.getCurrentNodeUrl())) {
            return store.get(key);
        }
        
        // If we are not in the preference list, forward to the primary (first in list)
        // Or try all in preference list until one responds.
        for (String nodeUrl : preferenceList) {
             try {
                 return fetchRemote(nodeUrl, key);
             } catch (Exception e) {
                 // Try next node
             }
        }
        throw new RuntimeException("Key not found or nodes unreachable");
    }

    public void put(String key, String value) {
        List<String> preferenceList = router.getPreferenceList(key, appConfig.getReplicationFactor());
        
        // Coordinate the write: send to all replicas
        boolean successfulWrite = false;
        
        for (String nodeUrl : preferenceList) {
            if (nodeUrl.equals(appConfig.getCurrentNodeUrl())) {
                store.put(key, value);
                successfulWrite = true;
            } else {
                try {
                    sendRemotePut(nodeUrl, key, value);
                    successfulWrite = true; // Count remote write as success for now
                } catch (Exception e) {
                    System.err.println("Failed to replicate to " + nodeUrl + ": " + e.getMessage());
                }
            }
        }
        
        if (!successfulWrite) {
            throw new RuntimeException("Failed to write to any node");
        }
    }
    
    // Internal method to handle direct storage (called by other nodes for replication)
    public void internalPut(String key, String value) {
        store.put(key, value);
    }

    private String fetchRemote(String nodeUrl, String key) {
        // Ensure URL format
        String url = nodeUrl + "/internal/get?key=" + key;
        return restTemplate.getForObject(url, String.class);
    }

    private void sendRemotePut(String nodeUrl, String key, String value) {
        String url = nodeUrl + "/internal/put";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Simple DTO or map
        Map<String, String> body = Map.of("key", key, "value", value);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(url, request, Void.class);
    }
}
