package com.example.crawler.controller;

import com.example.crawler.model.CrawledPage;
import com.example.crawler.service.CrawlerService;
import com.hazelcast.map.IMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startCrawl(@RequestParam String url, @RequestParam(defaultValue = "10") int maxPages) {
        crawlerService.startCrawl(url, maxPages);
        return ResponseEntity.ok("Crawler started for URL: " + url);
    }

    @GetMapping("/results")
    public ResponseEntity<Map<String, CrawledPage>> getResults() {
        IMap<String, CrawledPage> crawledPages = crawlerService.getCrawledPages();
        // Return local copy of the map
        return ResponseEntity.ok(Map.copyOf(crawledPages));
    }
}
