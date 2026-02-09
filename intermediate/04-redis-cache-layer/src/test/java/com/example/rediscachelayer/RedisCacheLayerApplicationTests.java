package com.example.rediscachelayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RedisCacheLayerApplicationTests {

    @Container
    public static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
            .withExposedPorts(6379);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void redisProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Test
    void testCacheHit() {
        String key = "testKey";
        String url = "http://localhost:" + port + "/data/" + key;

        // First call - should be a cache miss and take time
        long start1 = System.currentTimeMillis();
        String response1 = restTemplate.getForObject(url, String.class);
        long end1 = System.currentTimeMillis();
        long duration1 = end1 - start1;

        assertThat(response1).contains("Data for " + key);
        assertThat(duration1).isGreaterThanOrEqualTo(3000);

        // Second call - should be a cache hit and be fast
        long start2 = System.currentTimeMillis();
        String response2 = restTemplate.getForObject(url, String.class);
        long end2 = System.currentTimeMillis();
        long duration2 = end2 - start2;

        assertThat(response2).contains("Data for " + key);
        assertThat(duration2).isLessThan(1000); // Should be much faster than 3s
    }
}
