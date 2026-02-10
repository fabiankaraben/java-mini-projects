package com.fabiankaraben.djl;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inference")
public class InferenceController {

    private final InferenceService inferenceService;

    public InferenceController(InferenceService inferenceService) {
        this.inferenceService = inferenceService;
    }

    @PostMapping("/classify")
    public ResponseEntity<List<PredictionResponse>> classifyImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Classifications.Classification> predictions = inferenceService.predict(file.getInputStream());
            
            List<PredictionResponse> response = predictions.stream()
                    .map(p -> new PredictionResponse(p.getClassName(), p.getProbability()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (IOException | TranslateException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class PredictionResponse {
        private String className;
        private double probability;

        public PredictionResponse(String className, double probability) {
            this.className = className;
            this.probability = probability;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }
    }
}
