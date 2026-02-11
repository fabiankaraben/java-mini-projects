package com.example.distributedcounter;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/counter")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping("/{key}/add")
    public void add(@PathVariable String key, @RequestParam String element) {
        counterService.add(key, element);
    }

    @GetMapping("/{key}/count")
    public Map<String, Long> count(@PathVariable String key) {
        return Map.of("count", counterService.count(key));
    }
    
    @PostMapping("/merge")
    public void merge(@RequestParam String destination, @RequestParam String[] sources) {
        counterService.merge(destination, sources);
    }
    
    @DeleteMapping("/{key}")
    public void delete(@PathVariable String key) {
        counterService.delete(key);
    }
}
