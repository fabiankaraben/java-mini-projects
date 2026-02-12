package com.example.syntaxhighlighter.controller;

import com.example.syntaxhighlighter.model.HighlightRequest;
import com.example.syntaxhighlighter.service.SyntaxHighlightService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SyntaxHighlightController {

    private final SyntaxHighlightService syntaxHighlightService;

    public SyntaxHighlightController(SyntaxHighlightService syntaxHighlightService) {
        this.syntaxHighlightService = syntaxHighlightService;
    }

    @PostMapping(value = "/highlight", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> highlightCode(@RequestBody HighlightRequest request) {
        String html = syntaxHighlightService.highlight(request.getCode(), request.getLanguage());
        return ResponseEntity.ok(html);
    }
}
