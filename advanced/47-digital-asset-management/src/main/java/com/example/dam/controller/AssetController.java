package com.example.dam.controller;

import com.example.dam.model.Asset;
import com.example.dam.service.AssetService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Asset> uploadAsset(@RequestParam("file") MultipartFile file) {
        try {
            Asset asset = assetService.uploadAsset(file);
            // Don't return the data in the JSON response to keep it light
            asset.setData(null); 
            return ResponseEntity.status(HttpStatus.CREATED).body(asset);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetMetadata(@PathVariable Long id) {
        return assetService.getAsset(id)
                .map(asset -> {
                    asset.setData(null);
                    return ResponseEntity.ok(asset);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadAsset(@PathVariable Long id) {
        return assetService.getAsset(id)
                .map(asset -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + asset.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(asset.getContentType()))
                        .body(asset.getData()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history/{filename}")
    public ResponseEntity<List<Asset>> getAssetHistory(@PathVariable String filename) {
        List<Asset> history = assetService.getAssetHistory(filename);
        history.forEach(a -> a.setData(null));
        return ResponseEntity.ok(history);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchByMetadata(@RequestParam String key, @RequestParam String value) {
        List<Asset> results = assetService.searchByMetadata(key, value);
        results.forEach(a -> a.setData(null));
        return ResponseEntity.ok(results);
    }
}
