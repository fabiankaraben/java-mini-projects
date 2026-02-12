package com.example.analytics;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    // In-memory storage for metrics. In a real world scenario, this would be a time-series DB.
    private final List<Metric> metrics = new CopyOnWriteArrayList<>();

    public void ingest(Metric metric) {
        metrics.add(metric);
    }

    public List<AggregatedData> getAggregatedStats() {
        // Group by Endpoint and Method
        Map<String, Map<String, List<Metric>>> grouped = metrics.stream()
                .collect(Collectors.groupingBy(Metric::getEndpoint,
                        Collectors.groupingBy(Metric::getMethod)));

        List<AggregatedData> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<Metric>>> endpointEntry : grouped.entrySet()) {
            String endpoint = endpointEntry.getKey();
            for (Map.Entry<String, List<Metric>> methodEntry : endpointEntry.getValue().entrySet()) {
                String method = methodEntry.getKey();
                List<Metric> methodMetrics = methodEntry.getValue();

                long count = methodMetrics.size();
                long errorCount = methodMetrics.stream().filter(Metric::isError).count();
                double errorRate = (double) errorCount / count;

                List<Long> latencies = methodMetrics.stream()
                        .map(Metric::getLatencyMs)
                        .sorted()
                        .collect(Collectors.toList());

                double avgLatency = latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
                double p95Latency = calculateP95(latencies);

                result.add(new AggregatedData(endpoint, method, count, errorRate, p95Latency, avgLatency));
            }
        }

        return result;
    }

    private double calculateP95(List<Long> sortedLatencies) {
        if (sortedLatencies.isEmpty()) {
            return 0.0;
        }
        int index = (int) Math.ceil(0.95 * sortedLatencies.size()) - 1;
        // Ensure index is within bounds (if size=1, ceil(0.95)=1, index=0)
        index = Math.max(0, Math.min(index, sortedLatencies.size() - 1));
        return sortedLatencies.get(index);
    }
    
    // Helper to clear data for tests
    public void clear() {
        metrics.clear();
    }
}
