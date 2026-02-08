package com.example.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final int PORT = 8080;
    private static final JwtService jwtService = new JwtService();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/generate", new GenerateHandler());
        server.createContext("/validate", new ValidateHandler());
        
        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class GenerateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    Map<String, Object> requestBody = objectMapper.readValue(exchange.getRequestBody(), Map.class);
                    String subject = (String) requestBody.get("subject");
                    
                    if (subject == null || subject.isEmpty()) {
                        sendResponse(exchange, 400, "Missing subject");
                        return;
                    }

                    Map<String, Object> claims = new HashMap<>(requestBody);
                    claims.remove("subject"); // Subject is set separately

                    String token = jwtService.generateToken(subject, claims);
                    
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    
                    sendResponse(exchange, 200, objectMapper.writeValueAsString(response));
                } catch (Exception e) {
                    sendResponse(exchange, 500, "Error generating token: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }
        }
    }

    static class ValidateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) { // Changed to POST to easily send token in body
                try {
                    // Expecting {"token": "..."}
                    Map<String, String> requestBody = objectMapper.readValue(exchange.getRequestBody(), Map.class);
                    String token = requestBody.get("token");

                    if (token == null) {
                         sendResponse(exchange, 400, "Missing token");
                         return;
                    }

                    var claims = jwtService.validateToken(token);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("subject", claims.getSubject());
                    response.put("claims", claims);
                    
                    sendResponse(exchange, 200, objectMapper.writeValueAsString(response));
                } catch (Exception e) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", false);
                    response.put("error", e.getMessage());
                    try {
                        sendResponse(exchange, 401, objectMapper.writeValueAsString(response));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                sendResponse(exchange, 405, "Method not allowed");
            }
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
