package com.fabiankaraben.cachingwithmemcached;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class CachingWithMemcachedApplicationTests {

    @Container
    static GenericContainer<?> memcachedContainer = new GenericContainer<>(DockerImageName.parse("memcached:alpine"))
            .withExposedPorts(11211);

    @Autowired
    private CacheService cacheService;

    @DynamicPropertySource
    static void memcachedProperties(DynamicPropertyRegistry registry) {
        registry.add("memcached.host", memcachedContainer::getHost);
        registry.add("memcached.port", memcachedContainer::getFirstMappedPort);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testCacheSetAndGet() {
        String key = "testKey";
        String value = "testValue";
        
        cacheService.set(key, 3600, value);
        
        // Wait briefly for async set to propagate if needed, though spymemcached set is usually fast enough.
        // But for safety in tests:
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
             Object retrievedValue = cacheService.get(key);
             assertThat(retrievedValue).isEqualTo(value);
        });
    }

    @Test
    void testCacheExpiration() throws InterruptedException {
        String key = "expiredKey";
        String value = "expiredValue";
        
        // Set with 1 second expiration
        cacheService.set(key, 1, value);
        
        // Verify it exists initially
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            Object retrieved = cacheService.get(key);
            assertThat(retrieved).isEqualTo(value);
        });

        // Wait for expiration (slightly more than 1s)
        Thread.sleep(1500);

        // Verify it is gone
        Object expiredValue = cacheService.get(key);
        assertThat(expiredValue).isNull();
    }
    
    @Test
    void testCacheDelete() {
        String key = "deleteKey";
        String value = "deleteValue";
        
        cacheService.set(key, 3600, value);
        
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(cacheService.get(key)).isEqualTo(value);
        });
        
        cacheService.delete(key);
        
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
             assertThat(cacheService.get(key)).isNull();
        });
    }
}
