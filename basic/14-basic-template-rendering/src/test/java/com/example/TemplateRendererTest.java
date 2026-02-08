package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateRendererTest {

    private TemplateRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new TemplateRenderer();
    }

    @Test
    void testRenderStringWithSimplePlaceholders() {
        String template = "Hello, {{name}}!";
        Map<String, String> data = new HashMap<>();
        data.put("name", "World");

        String result = renderer.renderString(template, data);
        assertEquals("Hello, World!", result);
    }

    @Test
    void testRenderStringWithMultiplePlaceholders() {
        String template = "<h1>{{title}}</h1><p>{{message}}</p>";
        Map<String, String> data = new HashMap<>();
        data.put("title", "Welcome");
        data.put("message", "This is a test.");

        String result = renderer.renderString(template, data);
        assertEquals("<h1>Welcome</h1><p>This is a test.</p>", result);
    }

    @Test
    void testRenderStringWithMissingData() {
        String template = "Hello, {{name}}!";
        Map<String, String> data = new HashMap<>();
        // "name" is missing

        String result = renderer.renderString(template, data);
        assertEquals("Hello, {{name}}!", result);
    }

    @Test
    void testRenderWithClasspathResource() throws IOException {
        // We expect template.html to be in src/main/resources
        // template.html content:
        // <!DOCTYPE html>
        // <html lang="en">
        // <head>
        //     <meta charset="UTF-8">
        //     <title>{{title}}</title>
        // </head>
        // <body>
        //     <h1>Hello, {{name}}!</h1>
        //     <p>{{message}}</p>
        //     <footer>Generated at: {{timestamp}}</footer>
        // </body>
        // </html>

        Map<String, String> data = new HashMap<>();
        data.put("title", "Test Title");
        data.put("name", "Test User");
        data.put("message", "Test Message");
        data.put("timestamp", "2023-01-01");

        String result = renderer.render("template.html", data);

        assertNotNull(result);
        assertTrue(result.contains("<title>Test Title</title>"));
        assertTrue(result.contains("<h1>Hello, Test User!</h1>"));
        assertTrue(result.contains("<p>Test Message</p>"));
        assertTrue(result.contains("<footer>Generated at: 2023-01-01</footer>"));
    }
}
