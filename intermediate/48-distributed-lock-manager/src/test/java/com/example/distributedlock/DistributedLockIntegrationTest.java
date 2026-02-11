package com.example.distributedlock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class DistributedLockIntegrationTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
            .withExposedPorts(6379);

    @Autowired
    private DistributedLockService lockService;

    @Test
    void testConcurrentLockAcquisition() throws InterruptedException, ExecutionException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        String lockKey = "resource-1";
        long ttlSeconds = 5;

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(() -> lockService.acquireLock(lockKey, ttlSeconds));
        }

        List<Future<String>> futures = executorService.invokeAll(tasks);
        List<String> tokens = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);

        for (Future<String> future : futures) {
            String token = future.get();
            if (token != null) {
                successCount.incrementAndGet();
                tokens.add(token);
            }
        }

        // Only one thread should have acquired the lock
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(tokens).hasSize(1);

        // Clean up
        lockService.releaseLock(lockKey, tokens.get(0));
        executorService.shutdown();
    }

    @Test
    void testLockExpiration() throws InterruptedException {
        String lockKey = "resource-expire";
        long ttlSeconds = 1;

        // Acquire lock
        String token = lockService.acquireLock(lockKey, ttlSeconds);
        assertThat(token).isNotNull();

        // Wait for expiration
        Thread.sleep(1500);

        // Try to acquire again - should succeed because previous lock expired
        String newToken = lockService.acquireLock(lockKey, ttlSeconds);
        assertThat(newToken).isNotNull();
        assertThat(newToken).isNotEqualTo(token);
    }
}
