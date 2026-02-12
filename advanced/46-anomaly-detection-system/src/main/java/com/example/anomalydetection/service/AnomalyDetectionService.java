package com.example.anomalydetection.service;

import com.example.anomalydetection.model.AnomalyResult;
import com.example.anomalydetection.model.DataPoint;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class AnomalyDetectionService {

    // Sliding window size
    private static final int WINDOW_SIZE = 50;
    // Threshold for Z-score (typically 2.0 or 3.0)
    private static final double Z_SCORE_THRESHOLD = 3.0;

    private final ConcurrentLinkedDeque<Double> window = new ConcurrentLinkedDeque<>();

    public AnomalyResult analyze(DataPoint dataPoint) {
        double value = dataPoint.getValue();
        
        // Not enough data to determine anomaly
        if (window.size() < 10) {
            addToWindow(value);
            return new AnomalyResult(value, 0.0, false, "Insufficient data for analysis");
        }

        // Calculate Mean and StdDev from current window
        double mean = calculateMean();
        double stdDev = calculateStdDev(mean);

        // Avoid division by zero
        double zScore = 0.0;
        if (stdDev != 0) {
            zScore = (value - mean) / stdDev;
        }

        boolean isAnomaly = Math.abs(zScore) > Z_SCORE_THRESHOLD;
        
        // Add to window for future calculations (even if it's an anomaly? 
        // usually we might want to exclude extreme outliers from the baseline, 
        // but for a simple sliding window, we'll add it)
        addToWindow(value);

        String message = isAnomaly ? "Anomaly detected!" : "Normal data point";
        return new AnomalyResult(value, zScore, isAnomaly, message);
    }

    private void addToWindow(double value) {
        if (window.size() >= WINDOW_SIZE) {
            window.pollFirst();
        }
        window.addLast(value);
    }

    private double calculateMean() {
        return window.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double calculateStdDev(double mean) {
        double variance = window.stream()
                .mapToDouble(val -> Math.pow(val - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }
    
    // For testing purposes
    public void clear() {
        window.clear();
    }
}
