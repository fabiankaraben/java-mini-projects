package com.fabiankaraben.dfs.controller;

import com.fabiankaraben.dfs.service.DataNodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/datanode")
public class DataNodeController {

    private final DataNodeService dataNodeService;

    public DataNodeController(DataNodeService dataNodeService) {
        this.dataNodeService = dataNodeService;
    }

    @PostMapping("/chunks/{chunkId}")
    public ResponseEntity<String> uploadChunk(@PathVariable String chunkId, @RequestBody byte[] data) {
        try {
            dataNodeService.storeChunk(chunkId, data);
            return ResponseEntity.ok("Chunk stored successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store chunk: " + e.getMessage());
        }
    }

    @GetMapping("/chunks/{chunkId}")
    public ResponseEntity<byte[]> downloadChunk(@PathVariable String chunkId) {
        try {
            byte[] data = dataNodeService.getChunk(chunkId);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
