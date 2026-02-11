package com.example.markdownconverter.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkdownServiceTest {

    private final MarkdownService markdownService = new MarkdownService();

    @Test
    void testConvertToHtml_SimpleText() {
        String markdown = "Hello **World**";
        String html = markdownService.convertToHtml(markdown);
        // trim to remove potential newlines added by renderer
        assertEquals("<p>Hello <strong>World</strong></p>", html.trim());
    }

    @Test
    void testConvertToHtml_Headers() {
        String markdown = "# Header 1";
        String html = markdownService.convertToHtml(markdown);
        assertEquals("<h1>Header 1</h1>", html.trim());
    }

    @Test
    void testConvertToHtml_List() {
        String markdown = "- Item 1\n- Item 2";
        String html = markdownService.convertToHtml(markdown);
        assertTrue(html.contains("<ul>"));
        assertTrue(html.contains("<li>Item 1</li>"));
        assertTrue(html.contains("<li>Item 2</li>"));
        assertTrue(html.contains("</ul>"));
    }

    @Test
    void testConvertToHtml_Tables() {
        String markdown = "| Header | Header |\n| --- | --- |\n| Cell | Cell |";
        String html = markdownService.convertToHtml(markdown);
        assertTrue(html.contains("<table>"));
        assertTrue(html.contains("<thead>"));
        assertTrue(html.contains("<tbody>"));
    }

    @Test
    void testConvertToHtml_Null() {
        assertEquals("", markdownService.convertToHtml(null));
    }
}
