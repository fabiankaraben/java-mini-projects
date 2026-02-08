package com.example.configyaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static AppConfig loadConfig(String path) throws IOException {
        return mapper.readValue(new File(path), AppConfig.class);
    }
    
    public static AppConfig loadConfigFromResources(String fileName) throws IOException {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found in resources: " + fileName);
            }
            return mapper.readValue(inputStream, AppConfig.class);
        }
    }
}
