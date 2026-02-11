package com.example.distributedlock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributedLockServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private DistributedLockService lockService;

    @Test
    void acquireLock_Success() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        String token = lockService.acquireLock("resource1", 10);

        assertNotNull(token);
        verify(valueOperations).setIfAbsent(eq("lock:resource1"), anyString(), eq(Duration.ofSeconds(10)));
    }

    @Test
    void acquireLock_Failure() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(false);

        String token = lockService.acquireLock("resource1", 10);

        assertNull(token);
    }

    @Test
    void releaseLock_Success() {
        when(redisTemplate.execute(any(DefaultRedisScript.class), any(List.class), any())).thenReturn(1L);

        boolean result = lockService.releaseLock("resource1", "token123");

        assertTrue(result);
        verify(redisTemplate).execute(any(DefaultRedisScript.class), eq(Collections.singletonList("lock:resource1")), eq("token123"));
    }

    @Test
    void releaseLock_Failure() {
        when(redisTemplate.execute(any(DefaultRedisScript.class), any(List.class), any())).thenReturn(0L);

        boolean result = lockService.releaseLock("resource1", "token123");

        assertFalse(result);
    }
}
