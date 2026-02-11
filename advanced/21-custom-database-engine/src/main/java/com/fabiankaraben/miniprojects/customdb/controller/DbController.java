package com.fabiankaraben.miniprojects.customdb.controller;

import com.fabiankaraben.miniprojects.customdb.engine.StorageEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/db")
public class DbController {

    private final StorageEngine storageEngine;

    public DbController(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @PostMapping("/{key}")
    public ResponseEntity<String> set(@PathVariable String key, @RequestBody String value) {
        try {
            storageEngine.set(key, value);
            return ResponseEntity.ok("Key set successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error writing to database: " + e.getMessage());
        }
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        String value = storageEngine.get(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }
}
