package com.example.syntaxhighlighter.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SyntaxHighlightServiceTest {

    private final SyntaxHighlightService service = new SyntaxHighlightService();

    @Test
    void testHighlightJava() {
        String code = "public class Test {}";
        String html = service.highlight(code, "java");
        
        assertNotNull(html);
        assertTrue(html.contains("<pre"), "Should contain pre tag");
        assertTrue(html.contains("public"), "Should contain code content");
        assertTrue(html.contains("class"), "Should contain code content");
        assertTrue(html.contains("span style="), "Should contain span tags for highlighting");
    }

    @Test
    void testHighlightJson() {
        String code = "{\"key\": \"value\"}";
        String html = service.highlight(code, "json");
        
        assertNotNull(html);
        assertTrue(html.contains("key"), "Should contain key");
        assertTrue(html.contains("value"), "Should contain value");
        assertTrue(html.contains("span style="), "Should contain span tags");
    }

    @Test
    void testHighlightUnknownLanguage() {
        String code = "some random text";
        String html = service.highlight(code, "unknown");
        
        assertNotNull(html);
        assertTrue(html.contains("some"));
        assertTrue(html.contains("random"));
        assertTrue(html.contains("text"));
    }
}
