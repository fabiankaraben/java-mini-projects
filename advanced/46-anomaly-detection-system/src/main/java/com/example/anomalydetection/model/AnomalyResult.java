package com.example.anomalydetection.model;

public class AnomalyResult {
    private double value;
    private double zScore;
    private boolean isAnomaly;
    private String message;

    public AnomalyResult(double value, double zScore, boolean isAnomaly, String message) {
        this.value = value;
        this.zScore = zScore;
        this.isAnomaly = isAnomaly;
        this.message = message;
    }

    public double getValue() {
        return value;
    }

    public double getzScore() {
        return zScore;
    }

    public boolean isAnomaly() {
        return isAnomaly;
    }

    public String getMessage() {
        return message;
    }
}
