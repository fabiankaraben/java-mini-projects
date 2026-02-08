package com.example.ratelimiter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {
    // Allow 5 requests per second with a burst capacity of 10
    private static final TokenBucket rateLimiter = new TokenBucket(10, 5.0);

    public static void main(String[] args) throws IOException {
        startServer(8080);
    }

    public static HttpServer startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/api/resource", new RateLimitedHandler());
        server.setExecutor(null); // creates a default executor
        
        server.start();
        System.out.println("Server started on port " + port);
        return server;
    }

    static class RateLimitedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (rateLimiter.tryConsume()) {
                String response = "Request allowed";
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                String response = "Too Many Requests";
                exchange.sendResponseHeaders(429, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }
}
