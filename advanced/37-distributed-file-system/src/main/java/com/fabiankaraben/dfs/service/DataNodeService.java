package com.fabiankaraben.dfs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DataNodeService {

    @Value("${dfs.datanode.storage-dir:data}")
    private String storageDir;

    public void storeChunk(String chunkId, byte[] data) throws IOException {
        Path path = Paths.get(storageDir, chunkId);
        Files.createDirectories(path.getParent());
        Files.write(path, data);
    }

    public byte[] getChunk(String chunkId) throws IOException {
        Path path = Paths.get(storageDir, chunkId);
        if (!Files.exists(path)) {
            throw new IOException("Chunk not found: " + chunkId);
        }
        return Files.readAllBytes(path);
    }
}
