package com.example.cache;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        ComputationService expensiveService = new ExpensiveComputationService();
        ComputationService cachedService = new CachedComputationService(expensiveService);

        server.createContext("/compute", new ComputeHandler(cachedService));
        server.createContext("/stats", new StatsHandler((CachedComputationService) cachedService));

        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class ComputeHandler implements HttpHandler {
        private final ComputationService service;

        public ComputeHandler(ComputationService service) {
            this.service = service;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String input = null;
                if (query != null && query.startsWith("input=")) {
                    input = query.split("=")[1];
                }

                if (input == null || input.isEmpty()) {
                    String response = "Missing input parameter";
                    exchange.sendResponseHeaders(400, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(StandardCharsets.UTF_8));
                    }
                    return;
                }

                long startTime = System.currentTimeMillis();
                String result = service.compute(input);
                long endTime = System.currentTimeMillis();
                
                String response = String.format("Result: %s\nTime taken: %d ms", result, (endTime - startTime));
                
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
    
    static class StatsHandler implements HttpHandler {
        private final CachedComputationService service;

        public StatsHandler(CachedComputationService service) {
            this.service = service;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "Cache Size: " + service.getCacheSize();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
