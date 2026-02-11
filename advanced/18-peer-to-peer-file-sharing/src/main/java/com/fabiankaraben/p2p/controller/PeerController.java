package com.fabiankaraben.p2p.controller;

import com.fabiankaraben.p2p.service.FileService;
import com.fabiankaraben.p2p.service.PeerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PeerController {

    private final FileService fileService;
    private final PeerClient peerClient;

    public PeerController(FileService fileService, PeerClient peerClient) {
        this.fileService = fileService;
        this.peerClient = peerClient;
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() throws IOException {
        return ResponseEntity.ok(fileService.listFiles());
    }

    @PostMapping("/peers/download")
    public ResponseEntity<String> downloadFile(
            @RequestParam String host,
            @RequestParam int port,
            @RequestParam String fileName) {
        try {
            peerClient.downloadFile(host, port, fileName);
            return ResponseEntity.ok("File downloaded successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
