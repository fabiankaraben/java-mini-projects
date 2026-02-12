package com.fabiankaraben.dfs.model;

import java.util.List;
import java.util.UUID;

public class FileMetadata {
    private String fileId;
    private String filename;
    private long fileSize;
    private List<ChunkMetadata> chunks;

    public FileMetadata() {
        this.fileId = UUID.randomUUID().toString();
    }

    public FileMetadata(String filename, long fileSize, List<ChunkMetadata> chunks) {
        this();
        this.filename = filename;
        this.fileSize = fileSize;
        this.chunks = chunks;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public List<ChunkMetadata> getChunks() {
        return chunks;
    }

    public void setChunks(List<ChunkMetadata> chunks) {
        this.chunks = chunks;
    }
}
