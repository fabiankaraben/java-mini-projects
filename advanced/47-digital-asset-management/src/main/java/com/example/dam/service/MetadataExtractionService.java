package com.example.dam.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetadataExtractionService {

    public Map<String, String> extractMetadata(byte[] fileData) {
        Map<String, String> metadataMap = new HashMap<>();
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    // Storing as "Directory Name - Tag Name" -> Value
                    // This creates keys like "Exif SubIFD - ISO Speed Ratings"
                    String key = directory.getName() + " - " + tag.getTagName();
                    String value = tag.getDescription();
                    if (value != null && !value.isBlank()) {
                        metadataMap.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            // In a real system, we might want to log this or throw a specific exception.
            // For now, we just return empty metadata or partial metadata if extraction fails.
            System.err.println("Failed to extract metadata: " + e.getMessage());
        }
        return metadataMap;
    }
}
