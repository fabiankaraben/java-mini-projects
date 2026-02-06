package com.fabiankaraben.queryparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(QueryHandler.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final QueryParser queryParser = new QueryParser();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        logger.info(String.format("Received %s request for %s with query: %s", method, path, query));

        if ("GET".equals(method)) {
            handleGetRequest(exchange, query);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed", "text/plain");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String query) throws IOException {
        try {
            Map<String, String> params = queryParser.parse(query);
            String jsonResponse = objectMapper.writeValueAsString(params);
            sendResponse(exchange, 200, jsonResponse, "application/json");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing query parameters", e);
            sendResponse(exchange, 500, "Internal Server Error", "text/plain");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
