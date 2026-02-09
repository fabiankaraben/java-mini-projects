package com.fabiankaraben.sitemap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SitemapGeneratorTest {

    @Test
    void testGenerateXml() {
        SitemapGenerator generator = new SitemapGenerator("http://example.com");
        generator.addPage("page1");
        generator.addPage("/page2");

        String xml = generator.generateXml();

        assertNotNull(xml);
        assertTrue(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xml.contains("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"));
        assertTrue(xml.contains("<loc>http://example.com/page1</loc>"));
        assertTrue(xml.contains("<loc>http://example.com/page2</loc>"));
        assertTrue(xml.contains("<changefreq>daily</changefreq>"));
        assertTrue(xml.contains("<priority>0.8</priority>"));
        assertTrue(xml.contains("</urlset>"));
    }

    @Test
    void testBaseUrlTrailingSlash() {
        SitemapGenerator generator = new SitemapGenerator("http://example.com/");
        generator.addPage("page1");
        
        String xml = generator.generateXml();
        
        assertTrue(xml.contains("<loc>http://example.com/page1</loc>"));
        assertFalse(xml.contains("<loc>http://example.com//page1</loc>"));
    }
}
