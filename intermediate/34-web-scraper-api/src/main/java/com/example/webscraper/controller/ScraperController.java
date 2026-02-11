package com.example.webscraper.controller;

import com.example.webscraper.model.ScrapedData;
import com.example.webscraper.service.ScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ScraperController {

    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/scrape")
    public ScrapedData scrape(@RequestParam String url) throws IOException {
        return scraperService.scrape(url);
    }
}
