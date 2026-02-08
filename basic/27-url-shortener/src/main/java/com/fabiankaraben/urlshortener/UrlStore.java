package com.fabiankaraben.urlshortener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UrlStore {
    private final Map<String, String> idToUrl = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1000); // Start from a base number

    // Base62 characters
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    public String shorten(String originalUrl) {
        // Simple strategy: generate a unique ID using a counter and encode it to Base62
        long id = counter.getAndIncrement();
        String shortId = encode(id);
        idToUrl.put(shortId, originalUrl);
        return shortId;
    }

    public String getOriginalUrl(String id) {
        return idToUrl.get(id);
    }

    private String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
