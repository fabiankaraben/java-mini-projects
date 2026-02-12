package com.example.analytics;

public class AggregatedData {
    private String endpoint;
    private String method;
    private long requestCount;
    private double errorRate;
    private double p95Latency;
    private double averageLatency;

    public AggregatedData() {
    }

    public AggregatedData(String endpoint, String method, long requestCount, double errorRate, double p95Latency, double averageLatency) {
        this.endpoint = endpoint;
        this.method = method;
        this.requestCount = requestCount;
        this.errorRate = errorRate;
        this.p95Latency = p95Latency;
        this.averageLatency = averageLatency;
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

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public double getP95Latency() {
        return p95Latency;
    }

    public void setP95Latency(double p95Latency) {
        this.p95Latency = p95Latency;
    }

    public double getAverageLatency() {
        return averageLatency;
    }

    public void setAverageLatency(double averageLatency) {
        this.averageLatency = averageLatency;
    }
}
