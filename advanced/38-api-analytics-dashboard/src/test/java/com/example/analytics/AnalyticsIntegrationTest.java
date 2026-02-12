package com.example.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalyticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        analyticsService.clear();
    }

    @Test
    void testIngestAndAggregationLogic() throws Exception {
        // Ingest data: 100 requests. 90 are fast (10ms), 10 are slow (100ms).
        // This ensures P95 falls into the slow bucket.
        // Index 0-89: 10ms
        // Index 90-99: 100ms
        // P95 calculation for size 100: index 94 (95th item).
        // Index 94 is 100ms.

        List<Metric> metrics = new ArrayList<>();
        // Add 90 requests with latency 10
        for (int i = 0; i < 90; i++) {
            metrics.add(new Metric("/api/test", "GET", 10, false));
        }
        // Add 10 requests with latency 100
        for (int i = 0; i < 10; i++) {
            metrics.add(new Metric("/api/test", "GET", 100, false));
        }
        
        // Shuffle to simulate random arrival
        Collections.shuffle(metrics);

        for (Metric metric : metrics) {
            mockMvc.perform(post("/api/analytics/ingest")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(metric)))
                    .andExpect(status().isOk());
        }

        // Verify aggregation
        // Total: 100
        // P95: The 95th percentile of 100 items is the item at rank ceil(0.95 * 100) = 95.
        // Sorted latencies: [10, 10, ..., 10 (95 times), 100, 100, 100, 100, 100]
        // Index 0 to 94 are 10. Index 95 to 99 are 100.
        // So P95 should be 100.

        mockMvc.perform(get("/api/analytics/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].endpoint", is("/api/test")))
                .andExpect(jsonPath("$[0].requestCount", is(100)))
                .andExpect(jsonPath("$[0].p95Latency", is(100.0)));
    }

    @Test
    void testLoadSimulation() throws Exception {
        int threadCount = 10;
        int requestsPerThread = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < requestsPerThread; j++) {
                    try {
                        Metric metric = new Metric("/api/load", "POST", (long) (Math.random() * 100), false);
                        mockMvc.perform(post("/api/analytics/ingest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(metric)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Verify total count
        mockMvc.perform(get("/api/analytics/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].endpoint", is("/api/load")))
                .andExpect(jsonPath("$[0].requestCount", is(threadCount * requestsPerThread)));
    }
}
