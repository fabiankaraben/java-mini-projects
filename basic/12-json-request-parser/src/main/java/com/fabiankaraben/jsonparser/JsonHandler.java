package com.fabiankaraben.jsonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JsonHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try (InputStream requestBody = exchange.getRequestBody()) {
            User user = objectMapper.readValue(requestBody, User.class);
            String validationError = validateUser(user);

            if (validationError != null) {
                sendResponse(exchange, 400, "Validation Error: " + validationError);
            } else {
                String response = "User received: " + user.toString();
                sendResponse(exchange, 200, response);
            }
        } catch (Exception e) {
            sendResponse(exchange, 400, "Invalid JSON format");
        }
    }

    private String validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return "Name cannot be empty";
        }
        if (user.getAge() <= 0) {
            return "Age must be greater than 0";
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return "Invalid email";
        }
        return null;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
