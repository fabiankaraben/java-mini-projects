package com.example.encryption.controller;

import com.example.encryption.service.EncryptionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final EncryptionService encryptionService;

    public FileController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, Object>> encryptFile(@RequestParam("file") MultipartFile file) {
        try {
            SecretKey key = encryptionService.generateKey();
            byte[] encryptedData = encryptionService.encrypt(file.getBytes(), key);
            String encodedKey = encryptionService.encodeKey(key);
            String encodedData = java.util.Base64.getEncoder().encodeToString(encryptedData);

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", file.getOriginalFilename());
            response.put("encryptedData", encodedData);
            response.put("key", encodedKey);
            response.put("algorithm", "AES");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptFile(@RequestBody Map<String, String> request) {
        try {
            String encodedData = request.get("encryptedData");
            String encodedKey = request.get("key");
            
            if (encodedData == null || encodedKey == null) {
                return ResponseEntity.badRequest().body("Missing 'encryptedData' or 'key'");
            }

            SecretKey key = encryptionService.decodeKey(encodedKey);
            byte[] encryptedBytes = java.util.Base64.getDecoder().decode(encodedData);
            byte[] decryptedBytes = encryptionService.decrypt(encryptedBytes, key);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"decrypted_file\"")
                    .body(decryptedBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
