package com.example.e2echat.service;

import com.example.e2echat.model.UserKey;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyService {
    private final ConcurrentHashMap<String, String> keyStore = new ConcurrentHashMap<>();

    public void registerKey(UserKey userKey) {
        keyStore.put(userKey.getUsername(), userKey.getPublicKey());
    }

    public String getKey(String username) {
        return keyStore.get(username);
    }
}
