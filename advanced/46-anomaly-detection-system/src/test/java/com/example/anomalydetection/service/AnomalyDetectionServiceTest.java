package com.example.anomalydetection.service;

import com.example.anomalydetection.model.AnomalyResult;
import com.example.anomalydetection.model.DataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnomalyDetectionServiceTest {

    private AnomalyDetectionService service;

    @BeforeEach
    void setUp() {
        service = new AnomalyDetectionService();
    }

    @Test
    void testAnomalyDetection() {
        // 1. Feed normal data (mean ~ 10.0, stdDev ~ 0)
        // We need at least 10 points to start analyzing.
        // We need a filled window (50) to have a stable baseline, 
        // though the service starts analyzing after 10.
        
        for (int i = 0; i < 50; i++) {
            DataPoint point = new DataPoint(10.0);
            AnomalyResult result = service.analyze(point);
            assertFalse(result.isAnomaly(), "Point " + i + " should not be an anomaly");
        }

        // 2. Feed a slight variation, still normal
        DataPoint normalPoint = new DataPoint(10.5);
        AnomalyResult normalResult = service.analyze(normalPoint);
        assertFalse(normalResult.isAnomaly(), "10.5 should be normal");

        // 3. Feed an outlier (e.g. 100.0)
        // With mean ~10 and very low std dev, 100 should be huge z-score.
        DataPoint outlier = new DataPoint(100.0);
        AnomalyResult outlierResult = service.analyze(outlier);
        
        assertTrue(outlierResult.isAnomaly(), "100.0 should be detected as anomaly");
        assertTrue(outlierResult.getzScore() > 3.0, "Z-score should be > 3.0");
    }

    @Test
    void testInsufficientData() {
        service.clear();
        for (int i = 0; i < 5; i++) {
            DataPoint point = new DataPoint(10.0);
            AnomalyResult result = service.analyze(point);
            assertFalse(result.isAnomaly());
            assertEquals("Insufficient data for analysis", result.getMessage());
        }
    }
}
