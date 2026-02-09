package com.example.rediscachelayer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/data/{key}")
    public String getData(@PathVariable String key) {
        long start = System.currentTimeMillis();
        String result = dataService.getData(key);
        long end = System.currentTimeMillis();
        return result + " (Time: " + (end - start) + "ms)";
    }
}
