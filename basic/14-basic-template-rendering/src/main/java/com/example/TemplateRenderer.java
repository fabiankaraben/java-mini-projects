package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TemplateRenderer {

    public String render(String templatePath, Map<String, String> data) throws IOException {
        String templateContent = loadTemplate(templatePath);
        return processTemplate(templateContent, data);
    }

    public String renderString(String templateContent, Map<String, String> data) {
        return processTemplate(templateContent, data);
    }

    private String loadTemplate(String path) throws IOException {
        // Try loading from classpath resource first
        var resource = getClass().getClassLoader().getResource(path);
        if (resource != null) {
            try {
                 return new String(resource.openStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IOException("Failed to read classpath resource: " + path, e);
            }
        }
        
        // Fallback to file system
        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            return Files.readString(filePath);
        }
        
        throw new IOException("Template file not found: " + path);
    }

    private String processTemplate(String content, Map<String, String> data) {
        String result = content;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue();
            result = result.replace(placeholder, value != null ? value : "");
        }
        return result;
    }
}
