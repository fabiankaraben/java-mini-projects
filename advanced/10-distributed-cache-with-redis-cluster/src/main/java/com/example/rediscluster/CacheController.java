package com.example.rediscluster;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/{key}")
    public String set(@PathVariable String key, @RequestBody String value) {
        cacheService.set(key, value);
        return "Key " + key + " set successfully";
    }

    @GetMapping("/{key}")
    public String get(@PathVariable String key) {
        return cacheService.get(key);
    }

    @DeleteMapping("/{key}")
    public String delete(@PathVariable String key) {
        cacheService.delete(key);
        return "Key " + key + " deleted successfully";
    }
}
