package com.example.dam.service;

import com.example.dam.model.Asset;
import com.example.dam.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final MetadataExtractionService metadataExtractionService;

    public AssetService(AssetRepository assetRepository, MetadataExtractionService metadataExtractionService) {
        this.assetRepository = assetRepository;
        this.metadataExtractionService = metadataExtractionService;
    }

    @Transactional
    public Asset uploadAsset(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        byte[] data = file.getBytes();
        
        // Versioning logic
        Optional<Asset> latestVersion = assetRepository.findTopByFilenameOrderByVersionDesc(filename);
        int newVersion = latestVersion.map(asset -> asset.getVersion() + 1).orElse(1);

        Asset asset = new Asset(filename, file.getContentType(), file.getSize(), data);
        asset.setVersion(newVersion);

        // Extract Metadata
        Map<String, String> metadata = metadataExtractionService.extractMetadata(data);
        asset.setMetadata(metadata);

        return assetRepository.save(asset);
    }

    public Optional<Asset> getAsset(Long id) {
        return assetRepository.findById(id);
    }

    public List<Asset> getAssetHistory(String filename) {
        return assetRepository.findByFilename(filename);
    }

    public List<Asset> searchByMetadata(String key, String value) {
        return assetRepository.findByMetadata(key, value);
    }
}
