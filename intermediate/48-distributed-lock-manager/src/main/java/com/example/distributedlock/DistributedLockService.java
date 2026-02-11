package com.example.distributedlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistributedLockService {

    private final StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "lock:";

    /**
     * Tries to acquire a distributed lock.
     *
     * @param key        The unique key for the resource to lock.
     * @param ttlSeconds Time to live for the lock in seconds (to prevent deadlocks if the holder crashes).
     * @return A unique lock token if acquired, or null if the lock is already held.
     */
    public String acquireLock(String key, long ttlSeconds) {
        String lockKey = LOCK_PREFIX + key;
        String token = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, token, Duration.ofSeconds(ttlSeconds));

        if (Boolean.TRUE.equals(success)) {
            log.info("Lock acquired for key: {} with token: {}", key, token);
            return token;
        } else {
            log.warn("Failed to acquire lock for key: {}", key);
            return null;
        }
    }

    /**
     * Releases the lock if the token matches.
     * Uses a Lua script to ensure atomicity of the get-and-delete operation.
     *
     * @param key   The unique key for the resource.
     * @param token The token received when acquiring the lock.
     * @return true if the lock was released, false if the token didn't match or lock didn't exist.
     */
    public boolean releaseLock(String key, String token) {
        String lockKey = LOCK_PREFIX + key;
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), token);
        
        boolean released = result != null && result == 1L;
        if (released) {
            log.info("Lock released for key: {} with token: {}", key, token);
        } else {
            log.warn("Failed to release lock for key: {}. Token mismatch or lock expired.", key);
        }
        return released;
    }
}
