package com.example.captcha;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaServer {

    private static final int PORT = 8080;
    // Store session ID -> Captcha Text
    private static final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/captcha", new CaptchaHandler());
        server.createContext("/validate", new ValidateHandler());
        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }
    
    // For testing purposes to stop the server if needed, though mostly we'll just kill the process or run in test
    public static HttpServer startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/captcha", new CaptchaHandler());
        server.createContext("/validate", new ValidateHandler());
        server.setExecutor(null);
        server.start();
        return server;
    }

    static class CaptchaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            CaptchaGenerator.CaptchaResult result = CaptchaGenerator.generate();
            String sessionId = UUID.randomUUID().toString();
            sessionStore.put(sessionId, result.getText());

            exchange.getResponseHeaders().set("Content-Type", "image/png");
            exchange.getResponseHeaders().set("Set-Cookie", "SESSIONID=" + sessionId + "; Path=/; HttpOnly");
            
            exchange.sendResponseHeaders(200, 0); // 0 means chunked encoding or just unknown length until written
            
            try (OutputStream os = exchange.getResponseBody()) {
                ImageIO.write(result.getImage(), "png", os);
            }
        }
    }

    static class ValidateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod()) && !"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // Get Session ID from Cookie
            String sessionId = null;
            List<String> cookies = exchange.getRequestHeaders().get("Cookie");
            if (cookies != null) {
                for (String cookieLine : cookies) {
                    String[] pairs = cookieLine.split(";");
                    for (String pair : pairs) {
                        String[] keyVal = pair.trim().split("=");
                        if (keyVal.length == 2 && "SESSIONID".equals(keyVal[0])) {
                            sessionId = keyVal[1];
                            break;
                        }
                    }
                }
            }

            if (sessionId == null || !sessionStore.containsKey(sessionId)) {
                String response = "Session not found or expired";
                exchange.sendResponseHeaders(403, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }

            // Get user input from query query or body? 
            // Simple approach: query param "code"
            String query = exchange.getRequestURI().getQuery();
            String userCode = null;
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyVal = pair.split("=");
                    if (keyVal.length == 2 && "code".equals(keyVal[0])) {
                        userCode = keyVal[1];
                        break;
                    }
                }
            }

            String validCode = sessionStore.get(sessionId);
            String response;
            int statusCode;

            if (userCode != null && userCode.equalsIgnoreCase(validCode)) {
                response = "Valid";
                statusCode = 200;
                // Invalidate session after successful use? Or keep it? usually invalidate.
                sessionStore.remove(sessionId);
            } else {
                response = "Invalid";
                statusCode = 400;
            }

            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
