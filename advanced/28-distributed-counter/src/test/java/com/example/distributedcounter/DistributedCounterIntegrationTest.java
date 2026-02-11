package com.example.distributedcounter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.closeTo;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class DistributedCounterIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testConcurrentCounting() throws InterruptedException {
        int numberOfThreads = 100;
        int operationsPerThread = 100;
        int totalExpectedUniqueElements = numberOfThreads * operationsPerThread;
        String counterKey = "concurrent-counter";
        
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        // Simulate concurrent unique additions
        IntStream.range(0, numberOfThreads).forEach(i -> executorService.submit(() -> {
            IntStream.range(0, operationsPerThread).forEach(j -> {
                try {
                    String uniqueElement = UUID.randomUUID().toString();
                    mockMvc.perform(post("/api/counter/" + counterKey + "/add")
                            .param("element", uniqueElement))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }));

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(1, TimeUnit.MINUTES);
        if (!finished) {
            throw new RuntimeException("Test timed out");
        }

        // Verify count with error margin (HyperLogLog standard error is usually < 1%)
        // For 10000 elements, it should be very close.
        // We use a generous margin here to avoid flakiness, e.g., 5%.
        double errorMargin = totalExpectedUniqueElements * 0.05; 
        
        try {
             mockMvc.perform(get("/api/counter/" + counterKey + "/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", closeTo((double) totalExpectedUniqueElements, errorMargin)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
