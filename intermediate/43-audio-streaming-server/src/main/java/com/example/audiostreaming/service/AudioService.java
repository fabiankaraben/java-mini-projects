package com.example.audiostreaming.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AudioService {

    private final Path audioStorageLocation;

    public AudioService() {
        // In a real app, this would be configured via properties.
        // For this mini-project, we'll serve from src/main/resources/audio or a volume mount.
        // To be flexible with Docker, let's look for a specific directory.
        this.audioStorageLocation = Paths.get("data").toAbsolutePath().normalize();
    }

    public Resource loadAudio(String fileName) {
        try {
            Path filePath = this.audioStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                // Fallback for development/testing if not in 'data' volume, try classpath
                // This is useful for unit tests or initial run without volume
                return new org.springframework.core.io.ClassPathResource("audio/" + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found " + fileName, e);
        }
    }
}
