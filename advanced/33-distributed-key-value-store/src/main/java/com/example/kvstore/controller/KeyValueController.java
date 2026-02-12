package com.example.kvstore.controller;

import com.example.kvstore.service.KeyValueService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class KeyValueController {

    private final KeyValueService keyValueService;

    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    // Client facing API
    @GetMapping("/api/kv")
    public String get(@RequestParam String key) {
        return keyValueService.get(key);
    }

    @PostMapping("/api/kv")
    public void put(@RequestBody Map<String, String> payload) {
        keyValueService.put(payload.get("key"), payload.get("value"));
    }

    // Internal API for node-to-node communication
    @GetMapping("/internal/get")
    public String internalGet(@RequestParam String key) {
        return keyValueService.get(key); // Re-use get logic which checks local store if responsible
    }

    @PostMapping("/internal/put")
    public void internalPut(@RequestBody Map<String, String> payload) {
        keyValueService.internalPut(payload.get("key"), payload.get("value"));
    }
}
