package com.example.analytics;

import java.time.Instant;

public class Metric {
    private String endpoint;
    private String method;
    private long latencyMs;
    private boolean isError;
    private Instant timestamp;

    public Metric() {
        this.timestamp = Instant.now();
    }

    public Metric(String endpoint, String method, long latencyMs, boolean isError) {
        this.endpoint = endpoint;
        this.method = method;
        this.latencyMs = latencyMs;
        this.isError = isError;
        this.timestamp = Instant.now();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
