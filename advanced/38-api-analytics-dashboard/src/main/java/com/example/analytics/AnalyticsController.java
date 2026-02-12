package com.example.analytics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<Void> ingestMetric(@RequestBody Metric metric) {
        if (metric.getTimestamp() == null) {
            metric = new Metric(metric.getEndpoint(), metric.getMethod(), metric.getLatencyMs(), metric.isError());
        }
        analyticsService.ingest(metric);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<AggregatedData>> getStats() {
        return ResponseEntity.ok(analyticsService.getAggregatedStats());
    }
}
