package com.example.markdownconverter.controller;

import com.example.markdownconverter.service.MarkdownService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/markdown")
public class MarkdownController {

    private final MarkdownService markdownService;

    public MarkdownController(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @PostMapping("/convert")
    public Map<String, String> convert(@RequestBody String markdown) {
        String html = markdownService.convertToHtml(markdown);
        return Map.of("html", html);
    }
}
