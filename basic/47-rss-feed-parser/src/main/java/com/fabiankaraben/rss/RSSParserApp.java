package com.fabiankaraben.rss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RSSParserApp {

    private static final int PORT = 8080;
    private static final RSSService rssService = new RSSService();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/parse", new RSSHandler());
        server.setExecutor(null);
        System.out.println("RSS Feed Parser Server started on port " + PORT);
        server.start();
    }

    static class RSSHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGet(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = queryToMap(query);
            
            if (!queryParams.containsKey("url")) {
                sendResponse(exchange, 400, "Missing 'url' query parameter");
                return;
            }

            String feedUrl = queryParams.get("url");
            try {
                List<FeedItem> items = rssService.parseFeed(feedUrl);
                String jsonResponse = objectMapper.writeValueAsString(items);
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                sendResponse(exchange, 200, jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Error parsing feed: " + e.getMessage());
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) {
                return result;
            }
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }
}
