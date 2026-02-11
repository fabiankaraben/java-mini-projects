package com.example.featureflag;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureFlagService {

    private final Map<String, Boolean> flags = new ConcurrentHashMap<>();

    public FeatureFlagService() {
        // Initialize with some default flags
        flags.put("new-feature", false);
        flags.put("beta-ui", true);
    }

    public boolean isEnabled(String featureName) {
        return flags.getOrDefault(featureName, false);
    }

    public void setFeature(String featureName, boolean enabled) {
        flags.put(featureName, enabled);
    }

    public Map<String, Boolean> getAllFlags() {
        return flags;
    }
    
    public void deleteFeature(String featureName) {
        flags.remove(featureName);
    }
}
