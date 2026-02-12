package com.fabiankaraben.dfs.model;

public class ChunkMetadata {
    private String chunkId;
    private int sequence;
    private String dataNodeUrl; // The URL of the node where this chunk is stored

    public ChunkMetadata() {
    }

    public ChunkMetadata(String chunkId, int sequence, String dataNodeUrl) {
        this.chunkId = chunkId;
        this.sequence = sequence;
        this.dataNodeUrl = dataNodeUrl;
    }

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDataNodeUrl() {
        return dataNodeUrl;
    }

    public void setDataNodeUrl(String dataNodeUrl) {
        this.dataNodeUrl = dataNodeUrl;
    }
}
