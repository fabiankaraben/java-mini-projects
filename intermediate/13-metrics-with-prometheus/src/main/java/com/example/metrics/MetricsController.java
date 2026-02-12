package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MetricsController {

    private final Counter requestCounter;
    private final Timer randomDelayTimer;
    private final Random random = new Random();

    public MetricsController(MeterRegistry registry) {
        this.requestCounter = Counter.builder("custom.api.requests")
                .description("Number of requests to the custom endpoint")
                .register(registry);

        this.randomDelayTimer = Timer.builder("custom.api.delay")
                .description("Time taken to process the request with random delay")
                .register(registry);
    }

    @GetMapping("/hello")
    public String hello() {
        requestCounter.increment();
        return randomDelayTimer.record(() -> {
            try {
                // Simulate some work
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Hello from Prometheus Metrics App!";
        });
    }
}
