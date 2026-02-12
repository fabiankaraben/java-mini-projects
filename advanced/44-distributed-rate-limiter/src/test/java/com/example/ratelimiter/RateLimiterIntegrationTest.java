package com.example.ratelimiter;

import com.example.ratelimiter.service.RateLimiterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class RateLimiterIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private RateLimiterService rateLimiterService;

    @Test
    void testRateLimiterRespectsLimit() {
        String clientId = "test-client";
        int limit = 5;
        long windowMs = 1000;

        // Perform 'limit' allowed requests
        for (int i = 0; i < limit; i++) {
            assertTrue(rateLimiterService.isAllowed(clientId, limit, windowMs), "Request " + i + " should be allowed");
        }

        // The next request should be denied
        boolean exceeded = !rateLimiterService.isAllowed(clientId, limit, windowMs);
        assertTrue(exceeded, "Request should be rate limited");
    }

    @Test
    void testDistributedLoad() throws InterruptedException {
        String clientId = "distributed-client";
        int limit = 20;
        long windowMs = 2000;
        int numberOfThreads = 50; // More than the limit

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger allowedCount = new AtomicInteger(0);
        AtomicInteger blockedCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    if (rateLimiterService.isAllowed(clientId, limit, windowMs)) {
                        allowedCount.incrementAndGet();
                    } else {
                        blockedCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println("Allowed: " + allowedCount.get());
        System.out.println("Blocked: " + blockedCount.get());

        assertEquals(limit, allowedCount.get(), "Only " + limit + " requests should be allowed");
        assertEquals(numberOfThreads - limit, blockedCount.get(), "The rest should be blocked");
    }
}
