package com.fabiankaraben.sitemap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SitemapGenerator {

    private final List<String> urls = new ArrayList<>();
    private final String baseUrl;

    public SitemapGenerator(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public void addPage(String path) {
        String fullUrl = baseUrl + (path.startsWith("/") ? path : "/" + path);
        urls.add(fullUrl);
    }

    public String generateXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        for (String url : urls) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(url).append("</loc>\n");
            xml.append("    <lastmod>").append(today).append("</lastmod>\n");
            xml.append("    <changefreq>daily</changefreq>\n");
            xml.append("    <priority>0.8</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }
}
