package com.example.sitemap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SitemapGenerator {
    
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String URLSET_OPEN = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
    private static final String URLSET_CLOSE = "</urlset>";
    
    public static class UrlEntry {
        private final String loc;
        private final String lastmod;
        private final String changefreq;
        private final String priority;
        
        public UrlEntry(String loc, String lastmod, String changefreq, String priority) {
            this.loc = loc;
            this.lastmod = lastmod;
            this.changefreq = changefreq;
            this.priority = priority;
        }
        
        public String getLoc() {
            return loc;
        }
        
        public String getLastmod() {
            return lastmod;
        }
        
        public String getChangefreq() {
            return changefreq;
        }
        
        public String getPriority() {
            return priority;
        }
    }
    
    public String generateSitemap(List<UrlEntry> urls) {
        StringBuilder sitemap = new StringBuilder();
        sitemap.append(XML_HEADER).append("\n");
        sitemap.append(URLSET_OPEN).append("\n");
        
        for (UrlEntry url : urls) {
            sitemap.append("  <url>\n");
            sitemap.append("    <loc>").append(escapeXml(url.getLoc())).append("</loc>\n");
            
            if (url.getLastmod() != null && !url.getLastmod().isEmpty()) {
                sitemap.append("    <lastmod>").append(url.getLastmod()).append("</lastmod>\n");
            }
            
            if (url.getChangefreq() != null && !url.getChangefreq().isEmpty()) {
                sitemap.append("    <changefreq>").append(url.getChangefreq()).append("</changefreq>\n");
            }
            
            if (url.getPriority() != null && !url.getPriority().isEmpty()) {
                sitemap.append("    <priority>").append(url.getPriority()).append("</priority>\n");
            }
            
            sitemap.append("  </url>\n");
        }
        
        sitemap.append(URLSET_CLOSE);
        return sitemap.toString();
    }
    
    public String generateDefaultSitemap() {
        List<UrlEntry> defaultUrls = List.of(
            new UrlEntry("https://example.com/", getCurrentDate(), "daily", "1.0"),
            new UrlEntry("https://example.com/about", getCurrentDate(), "monthly", "0.8"),
            new UrlEntry("https://example.com/products", getCurrentDate(), "weekly", "0.9"),
            new UrlEntry("https://example.com/contact", getCurrentDate(), "yearly", "0.5")
        );
        return generateSitemap(defaultUrls);
    }
    
    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
    
    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
