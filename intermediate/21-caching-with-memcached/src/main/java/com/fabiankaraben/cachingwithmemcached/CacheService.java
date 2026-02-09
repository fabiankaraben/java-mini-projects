package com.fabiankaraben.cachingwithmemcached;

import net.spy.memcached.MemcachedClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class CacheService {

    private final MemcachedClient memcachedClient;

    public CacheService(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public void set(String key, int expiration, Object value) {
        memcachedClient.set(key, expiration, value);
    }

    public Object get(String key) {
        return memcachedClient.get(key);
    }

    public void delete(String key) {
        memcachedClient.delete(key);
    }
    
    // Asynchronous get with timeout
    public Object get(String key, long timeout, TimeUnit unit) {
        Future<Object> future = memcachedClient.asyncGet(key);
        try {
            return future.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            future.cancel(false);
            return null;
        }
    }
}
