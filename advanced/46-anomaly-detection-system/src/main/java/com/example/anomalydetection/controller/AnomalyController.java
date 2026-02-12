package com.example.anomalydetection.controller;

import com.example.anomalydetection.model.AnomalyResult;
import com.example.anomalydetection.model.DataPoint;
import com.example.anomalydetection.service.AnomalyDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anomaly")
public class AnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    public AnomalyController(AnomalyDetectionService anomalyDetectionService) {
        this.anomalyDetectionService = anomalyDetectionService;
    }

    @PostMapping("/detect")
    public ResponseEntity<AnomalyResult> detect(@RequestBody DataPoint dataPoint) {
        AnomalyResult result = anomalyDetectionService.analyze(dataPoint);
        return ResponseEntity.ok(result);
    }
}
