package com.example.chaos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ChaosLoader {
    private static final String CONFIG_FILE_ENV = "CHAOS_CONFIG_FILE";
    private static final String DEFAULT_CONFIG_FILE = "chaos-config.json";

    public static ChaosConfig loadConfig() {
        String configPath = System.getenv(CONFIG_FILE_ENV);
        if (configPath == null || configPath.isEmpty()) {
            configPath = DEFAULT_CONFIG_FILE;
        }

        File file = new File(configPath);
        if (!file.exists()) {
            System.err.println("[ChaosAgent] Config file not found: " + configPath);
            return new ChaosConfig();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, ChaosConfig.class);
        } catch (IOException e) {
            System.err.println("[ChaosAgent] Failed to load config: " + e.getMessage());
            e.printStackTrace();
            return new ChaosConfig();
        }
    }
}
