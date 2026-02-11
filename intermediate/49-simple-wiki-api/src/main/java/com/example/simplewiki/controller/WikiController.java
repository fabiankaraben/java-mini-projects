package com.example.simplewiki.controller;

import com.example.simplewiki.model.PageVersion;
import com.example.simplewiki.model.WikiPage;
import com.example.simplewiki.service.WikiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pages")
public class WikiController {

    private final WikiService wikiService;

    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @PostMapping
    public ResponseEntity<WikiPage> createPage(@RequestBody Map<String, String> payload) {
        String slug = payload.get("slug");
        String title = payload.get("title");
        String content = payload.get("content");

        if (slug == null || title == null || content == null) {
            return ResponseEntity.badRequest().build();
        }

        WikiPage page = wikiService.createPage(slug, title, content);
        return ResponseEntity.created(URI.create("/api/pages/" + slug)).body(page);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<WikiPage> updatePage(@PathVariable String slug, @RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        if (content == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            WikiPage page = wikiService.updatePage(slug, content);
            return ResponseEntity.ok(page);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<WikiPage> getPage(@PathVariable String slug) {
        return wikiService.getPageMetadata(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{slug}/content")
    public ResponseEntity<String> getLatestContent(@PathVariable String slug) {
        return wikiService.getLatestContent(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{slug}/history")
    public ResponseEntity<List<PageVersion>> getHistory(@PathVariable String slug) {
        try {
            return ResponseEntity.ok(wikiService.getHistory(slug));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{slug}/versions/{version}")
    public ResponseEntity<String> getVersionContent(@PathVariable String slug, @PathVariable Integer version) {
        return wikiService.getVersionContent(slug, version)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
