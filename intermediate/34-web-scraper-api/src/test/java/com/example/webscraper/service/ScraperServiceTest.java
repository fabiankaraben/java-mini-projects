package com.example.webscraper.service;

import com.example.webscraper.model.ScrapedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScraperServiceTest {

    private final ScraperService scraperService = new ScraperService();

    @Test
    void testExtractData() {
        String html = "<html><head><title>Test Page</title>" +
                "<meta name='description' content='A test page for scraper'>" +
                "<meta property='og:title' content='Test Open Graph'>" +
                "</head><body>" +
                "<h1>Main Heading</h1>" +
                "<h2>Sub Heading 1</h2>" +
                "<h2>Sub Heading 2</h2>" +
                "<p>Some paragraph text.</p>" +
                "</body></html>";

        Document doc = Jsoup.parse(html);
        ScrapedData data = scraperService.extractData(doc);

        assertEquals("Test Page", data.getTitle());
        
        Map<String, String> metaTags = data.getMetaTags();
        assertEquals("A test page for scraper", metaTags.get("description"));
        assertEquals("Test Open Graph", metaTags.get("og:title"));

        Map<String, List<String>> headers = data.getHeaders();
        assertTrue(headers.containsKey("h1"));
        assertTrue(headers.containsKey("h2"));
        assertFalse(headers.containsKey("h3"));

        assertEquals(1, headers.get("h1").size());
        assertEquals("Main Heading", headers.get("h1").get(0));

        assertEquals(2, headers.get("h2").size());
        assertEquals("Sub Heading 1", headers.get("h2").get(0));
        assertEquals("Sub Heading 2", headers.get("h2").get(1));
    }
}
