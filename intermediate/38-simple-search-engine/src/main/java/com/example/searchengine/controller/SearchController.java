package com.example.searchengine.controller;

import com.example.searchengine.model.Document;
import com.example.searchengine.service.InvertedIndexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final InvertedIndexService invertedIndexService;

    public SearchController(InvertedIndexService invertedIndexService) {
        this.invertedIndexService = invertedIndexService;
    }

    @PostMapping("/index")
    public ResponseEntity<String> indexDocument(@RequestBody Document document) {
        invertedIndexService.indexDocument(document);
        return ResponseEntity.ok("Document indexed successfully: " + document.getId());
    }

    @GetMapping
    public ResponseEntity<Set<String>> search(@RequestParam String query) {
        Set<String> results = invertedIndexService.search(query);
        return ResponseEntity.ok(results);
    }
}
