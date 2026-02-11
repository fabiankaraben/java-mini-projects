package com.example.urlhealthmonitor.controller;

import com.example.urlhealthmonitor.model.MonitoredUrl;
import com.example.urlhealthmonitor.model.UrlCheckHistory;
import com.example.urlhealthmonitor.service.UrlMonitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlMonitorService monitorService;

    public UrlController(UrlMonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @PostMapping
    public ResponseEntity<MonitoredUrl> addUrl(@RequestBody Map<String, String> payload) {
        String url = payload.get("url");
        if (url == null || url.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(monitorService.addUrl(url));
    }

    @GetMapping
    public List<MonitoredUrl> getAllUrls() {
        return monitorService.getAllUrls();
    }

    @GetMapping("/{id}/history")
    public List<UrlCheckHistory> getUrlHistory(@PathVariable Long id) {
        return monitorService.getHistory(id);
    }
}
