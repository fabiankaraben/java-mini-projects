package com.example.ratelimiting;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class RateLimitingIntegrationTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.12-alpine"));

    @DynamicPropertySource
    static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRateLimiting() throws Exception {
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger tooManyRequestsCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    int status = mockMvc.perform(get("/api/resource")
                            .header("X-User-Id", "test-user"))
                            .andReturn().getResponse().getStatus();
                    if (status == 200) {
                        successCount.incrementAndGet();
                    } else if (status == 429) {
                        tooManyRequestsCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Success count: " + successCount.get());
        System.out.println("Too many requests count: " + tooManyRequestsCount.get());

        // We expect at least some successes (capacity is 20)
        assert successCount.get() > 0;
        // We expect at least some failures (we sent 30 requests, capacity is 20 + rate 10/s, but instant burst might exceed)
        // Actually, with capacity 20 and rate 10, sending 30 requests instantly might all succeed if we don't burst fast enough, 
        // or if the test runs slow. But with 30 concurrent threads, we should hit the limit.
        // Wait, capacity is 20. We send 30. 
        // If they all arrive at the exact same millisecond, 20 pass, 10 fail.
        // Let's assert based on logical bounds.
        assert successCount.get() <= 21; // Capacity 20 + maybe 1 token generated during execution
        assert tooManyRequestsCount.get() >= 9;
    }
}
