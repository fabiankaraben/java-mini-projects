package com.fabiankaraben.dfs.service;

import com.fabiankaraben.dfs.model.ChunkMetadata;
import com.fabiankaraben.dfs.model.FileMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NameNodeService {

    private final Map<String, FileMetadata> metadataStore = new ConcurrentHashMap<>();
    private final List<String> dataNodes;
    private final RestTemplate restTemplate;
    
    // Configurable chunk size (default 1MB)
    @Value("${dfs.namenode.chunk-size:1048576}")
    private int chunkSize;

    public NameNodeService(@Value("${dfs.datanodes.urls}") List<String> dataNodes, RestTemplate restTemplate) {
        this.dataNodes = dataNodes;
        this.restTemplate = restTemplate;
    }

    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        byte[] bytes = file.getBytes();
        long fileSize = bytes.length;
        String filename = file.getOriginalFilename();

        List<ChunkMetadata> chunks = new ArrayList<>();
        int totalChunks = (int) Math.ceil((double) fileSize / chunkSize);

        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(bytes.length, start + chunkSize);
            byte[] chunkData = Arrays.copyOfRange(bytes, start, end);

            String chunkId = UUID.randomUUID().toString();
            // Simple Round-Robin distribution
            String targetNodeUrl = dataNodes.get(i % dataNodes.size());
            
            // Upload chunk to DataNode
            uploadChunkToDataNode(targetNodeUrl, chunkId, chunkData);

            chunks.add(new ChunkMetadata(chunkId, i, targetNodeUrl));
        }

        FileMetadata metadata = new FileMetadata(filename, fileSize, chunks);
        metadata.setFileId(fileId);
        metadataStore.put(fileId, metadata);
        
        return metadata;
    }

    private void uploadChunkToDataNode(String nodeUrl, String chunkId, byte[] data) {
        String url = nodeUrl + "/datanode/chunks/" + chunkId;
        restTemplate.postForEntity(url, data, String.class);
    }

    public byte[] getFile(String fileId) throws IOException {
        FileMetadata metadata = metadataStore.get(fileId);
        if (metadata == null) {
            throw new IOException("File not found: " + fileId);
        }

        // Sort chunks by sequence just in case
        metadata.getChunks().sort(Comparator.comparingInt(ChunkMetadata::getSequence));

        // Estimate size
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream((int) metadata.getFileSize());

        for (ChunkMetadata chunk : metadata.getChunks()) {
            byte[] chunkData = downloadChunkFromDataNode(chunk.getDataNodeUrl(), chunk.getChunkId());
            outputStream.write(chunkData);
        }

        return outputStream.toByteArray();
    }

    private byte[] downloadChunkFromDataNode(String nodeUrl, String chunkId) {
        String url = nodeUrl + "/datanode/chunks/" + chunkId;
        return restTemplate.getForObject(url, byte[].class);
    }

    public FileMetadata getFileMetadata(String fileId) {
        return metadataStore.get(fileId);
    }
}
