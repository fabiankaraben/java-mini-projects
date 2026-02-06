package com.fabiankaraben.jsonapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(JsonHandler.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        logger.info(String.format("Received %s request for %s", method, path));

        if ("GET".equals(method)) {
            handleGetRequest(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        try {
            // Create a sample user object
            User user = new User(1, "John Doe", "john.doe@example.com");

            // Serialize user to JSON
            String jsonResponse = objectMapper.writeValueAsString(user);

            // Send JSON response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 200, jsonResponse);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing JSON request", e);
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
