package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

public class CookieServer {

    private static final int PORT = 8080;
    private static final String COOKIE_NAME = "user_session";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new CookieHandler());
        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class CookieHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Check for Cookie header
            List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
            String foundCookieValue = null;

            if (cookieHeaders != null) {
                for (String header : cookieHeaders) {
                    // Cookie header format: name=value; name2=value2
                    String[] cookies = header.split(";");
                    for (String cookie : cookies) {
                        String[] parts = cookie.trim().split("=", 2);
                        if (parts.length == 2 && parts[0].equals(COOKIE_NAME)) {
                            foundCookieValue = parts[1];
                            break;
                        }
                    }
                    if (foundCookieValue != null) break;
                }
            }

            String response;
            int statusCode = 200;

            if (foundCookieValue != null) {
                response = "Welcome back! Found cookie: " + foundCookieValue;
            } else {
                // Set a new cookie
                String newCookieValue = UUID.randomUUID().toString();
                exchange.getResponseHeaders().add("Set-Cookie", COOKIE_NAME + "=" + newCookieValue + "; Path=/; HttpOnly");
                response = "Cookie set! Reload to see it.";
            }

            sendResponse(exchange, statusCode, response);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
