package com.fabiankaraben.miniprojects.customdb.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class StorageEngine {

    @Value("${db.data.file:data/db.log}")
    private String dbFilePath;

    private final Map<String, String> index = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private BufferedWriter writer;

    @PostConstruct
    public void init() throws IOException {
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            File parent = dbFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            dbFile.createNewFile();
        }

        // Replay log to build index
        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Simple format: key,value (comma separated, taking first comma)
                int separatorIndex = line.indexOf(',');
                if (separatorIndex > 0) {
                    String key = line.substring(0, separatorIndex);
                    String value = line.substring(separatorIndex + 1);
                    index.put(key, value);
                }
            }
        }

        // Open writer for appending
        writer = new BufferedWriter(new FileWriter(dbFile, StandardCharsets.UTF_8, true));
    }

    public void set(String key, String value) throws IOException {
        if (key == null || key.contains(",")) {
            throw new IllegalArgumentException("Key cannot be null or contain commas");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        // Sanitize value (remove newlines to keep one record per line)
        String sanitizedValue = value.replace("\n", " ").replace("\r", "");

        lock.writeLock().lock();
        try {
            writer.write(key + "," + sanitizedValue);
            writer.newLine();
            writer.flush(); // Ensure persistence immediately
            index.put(key, sanitizedValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) {
        return index.get(key);
    }

    @PreDestroy
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
