package com.example.sitemap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SitemapServer {
    
    private static final Logger logger = LoggerFactory.getLogger(SitemapServer.class);
    private static final int PORT = 8080;
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/sitemap.xml", new SitemapHandler());
        server.createContext("/custom-sitemap.xml", new CustomSitemapHandler());
        server.createContext("/health", new HealthHandler());
        
        server.setExecutor(null);
        server.start();
        
        logger.info("Sitemap Generator Server started on port {}", PORT);
        logger.info("Available endpoints:");
        logger.info("  GET http://localhost:{}/sitemap.xml - Default sitemap", PORT);
        logger.info("  POST http://localhost:{}/custom-sitemap.xml - Custom sitemap (JSON body)", PORT);
        logger.info("  GET http://localhost:{}/health - Health check", PORT);
    }
    
    static class SitemapHandler implements HttpHandler {
        private final SitemapGenerator generator = new SitemapGenerator();
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            
            try {
                String sitemap = generator.generateDefaultSitemap();
                exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=UTF-8");
                sendResponse(exchange, 200, sitemap);
                logger.info("Generated default sitemap");
            } catch (Exception e) {
                logger.error("Error generating sitemap", e);
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }
    }
    
    static class CustomSitemapHandler implements HttpHandler {
        private final SitemapGenerator generator = new SitemapGenerator();
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                List<SitemapGenerator.UrlEntry> urls = parseJsonUrls(requestBody);
                
                String sitemap = generator.generateSitemap(urls);
                exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=UTF-8");
                sendResponse(exchange, 200, sitemap);
                logger.info("Generated custom sitemap with {} URLs", urls.size());
            } catch (Exception e) {
                logger.error("Error generating custom sitemap", e);
                sendResponse(exchange, 400, "Bad Request: " + e.getMessage());
            }
        }
        
        private List<SitemapGenerator.UrlEntry> parseJsonUrls(String json) {
            List<SitemapGenerator.UrlEntry> urls = new ArrayList<>();
            
            json = json.trim();
            if (!json.startsWith("[") || !json.endsWith("]")) {
                throw new IllegalArgumentException("Expected JSON array");
            }
            
            json = json.substring(1, json.length() - 1).trim();
            if (json.isEmpty()) {
                return urls;
            }
            
            int braceCount = 0;
            StringBuilder currentObject = new StringBuilder();
            
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                
                if (c == '{') {
                    braceCount++;
                    currentObject.append(c);
                } else if (c == '}') {
                    braceCount--;
                    currentObject.append(c);
                    
                    if (braceCount == 0) {
                        urls.add(parseUrlEntry(currentObject.toString()));
                        currentObject = new StringBuilder();
                    }
                } else if (c == ',' && braceCount == 0) {
                    continue;
                } else {
                    currentObject.append(c);
                }
            }
            
            return urls;
        }
        
        private SitemapGenerator.UrlEntry parseUrlEntry(String json) {
            String loc = extractJsonValue(json, "loc");
            String lastmod = extractJsonValue(json, "lastmod");
            String changefreq = extractJsonValue(json, "changefreq");
            String priority = extractJsonValue(json, "priority");
            
            if (loc == null || loc.isEmpty()) {
                throw new IllegalArgumentException("URL 'loc' field is required");
            }
            
            return new SitemapGenerator.UrlEntry(loc, lastmod, changefreq, priority);
        }
        
        private String extractJsonValue(String json, String key) {
            String searchKey = "\"" + key + "\"";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) {
                return null;
            }
            
            int colonIndex = json.indexOf(":", keyIndex);
            if (colonIndex == -1) {
                return null;
            }
            
            int valueStart = colonIndex + 1;
            while (valueStart < json.length() && (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '\t')) {
                valueStart++;
            }
            
            if (valueStart >= json.length()) {
                return null;
            }
            
            if (json.charAt(valueStart) == '"') {
                int valueEnd = json.indexOf('"', valueStart + 1);
                if (valueEnd == -1) {
                    return null;
                }
                return json.substring(valueStart + 1, valueEnd);
            } else {
                int valueEnd = valueStart;
                while (valueEnd < json.length() && json.charAt(valueEnd) != ',' && json.charAt(valueEnd) != '}') {
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd).trim();
            }
        }
    }
    
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            
            String response = "{\"status\":\"healthy\",\"service\":\"sitemap-generator\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 200, response);
        }
    }
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
