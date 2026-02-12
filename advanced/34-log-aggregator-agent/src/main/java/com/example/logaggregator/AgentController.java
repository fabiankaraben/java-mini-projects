package com.example.logaggregator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AgentController {

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("agent", "Log Aggregator Agent");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }
}
