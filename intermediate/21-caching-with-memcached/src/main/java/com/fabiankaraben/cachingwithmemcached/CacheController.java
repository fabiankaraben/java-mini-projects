package com.fabiankaraben.cachingwithmemcached;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/{key}")
    public ResponseEntity<String> set(@PathVariable String key, @RequestBody String value, @RequestParam(defaultValue = "3600") int expiration) {
        cacheService.set(key, expiration, value);
        return ResponseEntity.ok("Key set");
    }

    @GetMapping("/{key}")
    public ResponseEntity<Object> get(@PathVariable String key) {
        Object value = cacheService.get(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> delete(@PathVariable String key) {
        cacheService.delete(key);
        return ResponseEntity.ok("Key deleted");
    }
}
