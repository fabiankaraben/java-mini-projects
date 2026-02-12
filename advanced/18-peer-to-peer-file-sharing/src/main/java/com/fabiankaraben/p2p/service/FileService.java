package com.fabiankaraben.p2p.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    private final Path storagePath;

    public FileService(@Value("${p2p.storage.path:./storage}") String storageDir) throws IOException {
        this.storagePath = Paths.get(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(this.storagePath);
    }

    public List<String> listFiles() throws IOException {
        try (Stream<Path> stream = Files.walk(this.storagePath, 1)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    public File getFile(String fileName) {
        Path filePath = this.storagePath.resolve(fileName).normalize();
        if (!filePath.startsWith(this.storagePath)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return file;
    }

    public void saveFile(String fileName, InputStream inputStream) throws IOException {
        Path filePath = this.storagePath.resolve(fileName).normalize();
        if (!filePath.startsWith(this.storagePath)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public String calculateChecksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
