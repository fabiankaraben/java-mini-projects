package com.fabiankaraben.formdata;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FormDataHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(FormDataHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        logger.info(String.format("Received %s request for %s", method, path));

        if ("POST".equals(method)) {
            handlePostRequest(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            // Check Content-Type
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            if (contentType == null || !contentType.contains("application/x-www-form-urlencoded")) {
                sendResponse(exchange, 415, "Unsupported Media Type. Expected application/x-www-form-urlencoded");
                return;
            }

            // Read request body
            String requestBody;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                requestBody = reader.lines().collect(Collectors.joining("\n"));
            }

            // Parse form data
            Map<String, String> formData = parseFormData(requestBody);

            // Construct response
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("Received Form Data:\n");
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                responseBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            sendResponse(exchange, 200, responseBuilder.toString());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing form data", e);
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private Map<String, String> parseFormData(String body) {
        Map<String, String> formData = new HashMap<>();
        if (body == null || body.isEmpty()) {
            return formData;
        }

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                formData.put(key, value);
            } else if (keyValue.length == 1) {
                 String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                 formData.put(key, "");
            }
        }
        return formData;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
