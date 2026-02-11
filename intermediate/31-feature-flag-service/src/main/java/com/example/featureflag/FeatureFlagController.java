package com.example.featureflag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/features")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @GetMapping
    public Map<String, Boolean> getAllFeatures() {
        return featureFlagService.getAllFlags();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Boolean> getFeatureStatus(@PathVariable String name) {
        return ResponseEntity.ok(featureFlagService.isEnabled(name));
    }

    @PostMapping("/{name}")
    public ResponseEntity<Void> enableFeature(@PathVariable String name) {
        featureFlagService.setFeature(name, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> disableFeature(@PathVariable String name) {
        featureFlagService.setFeature(name, false);
        return ResponseEntity.ok().build();
    }
}
