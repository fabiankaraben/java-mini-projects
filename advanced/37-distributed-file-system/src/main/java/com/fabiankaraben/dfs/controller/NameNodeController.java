package com.fabiankaraben.dfs.controller;

import com.fabiankaraben.dfs.model.FileMetadata;
import com.fabiankaraben.dfs.service.NameNodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/namenode")
public class NameNodeController {

    private final NameNodeService nameNodeService;

    public NameNodeController(NameNodeService nameNodeService) {
        this.nameNodeService = nameNodeService;
    }

    @PostMapping("/files")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileMetadata metadata = nameNodeService.uploadFile(file);
            return ResponseEntity.ok(metadata);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            FileMetadata metadata = nameNodeService.getFileMetadata(fileId);
            if (metadata == null) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] data = nameNodeService.getFile(fileId);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/files/{fileId}/metadata")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable String fileId) {
        FileMetadata metadata = nameNodeService.getFileMetadata(fileId);
        if (metadata == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(metadata);
    }
}
