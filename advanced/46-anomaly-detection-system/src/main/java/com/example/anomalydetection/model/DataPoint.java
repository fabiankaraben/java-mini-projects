package com.example.anomalydetection.model;

import java.time.Instant;

public class DataPoint {
    private double value;
    private Instant timestamp;

    public DataPoint() {
        this.timestamp = Instant.now();
    }

    public DataPoint(double value) {
        this.value = value;
        this.timestamp = Instant.now();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
