package com.example.kvstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kvstore")
public class AppConfig {
    private List<String> nodes = new ArrayList<>();
    private String currentNodeUrl;
    private int replicationFactor = 2;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getCurrentNodeUrl() {
        return currentNodeUrl;
    }

    public void setCurrentNodeUrl(String currentNodeUrl) {
        this.currentNodeUrl = currentNodeUrl;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}
