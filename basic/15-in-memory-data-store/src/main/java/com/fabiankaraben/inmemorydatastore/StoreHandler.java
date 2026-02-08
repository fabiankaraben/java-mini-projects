package com.fabiankaraben.inmemorydatastore;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class StoreHandler implements HttpHandler {
    private final DataStore dataStore;
    private final Gson gson;

    public StoreHandler(DataStore dataStore) {
        this.dataStore = dataStore;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Remove trailing slash if present for consistent matching
        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }

        try {
            if (path.equals("/items")) {
                handleCollection(exchange, method);
            } else if (path.startsWith("/items/")) {
                handleItem(exchange, method, path);
            } else {
                sendResponse(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                String jsonResponse = gson.toJson(dataStore.findAll());
                sendResponse(exchange, 200, jsonResponse);
                break;
            case "POST":
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Item newItem = gson.fromJson(reader, Item.class);
                if (newItem.getId() == null || newItem.getContent() == null) {
                    sendResponse(exchange, 400, "Missing id or content");
                    return;
                }
                if (dataStore.findById(newItem.getId()).isPresent()) {
                    sendResponse(exchange, 409, "Item with ID already exists");
                    return;
                }
                dataStore.save(newItem);
                sendResponse(exchange, 201, gson.toJson(newItem));
                break;
            default:
                sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleItem(HttpExchange exchange, String method, String path) throws IOException {
        String id = path.substring("/items/".length());
        if (id.isEmpty()) {
            sendResponse(exchange, 404, "Not Found");
            return;
        }

        switch (method) {
            case "GET":
                Optional<Item> item = dataStore.findById(id);
                if (item.isPresent()) {
                    sendResponse(exchange, 200, gson.toJson(item.get()));
                } else {
                    sendResponse(exchange, 404, "Item not found");
                }
                break;
            case "PUT":
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                Item updateItem = gson.fromJson(reader, Item.class);
                if (!id.equals(updateItem.getId())) {
                    sendResponse(exchange, 400, "ID in path does not match ID in body");
                    return;
                }
                if (dataStore.findById(id).isEmpty()) {
                    sendResponse(exchange, 404, "Item not found");
                    return;
                }
                dataStore.save(updateItem);
                sendResponse(exchange, 200, gson.toJson(updateItem));
                break;
            case "DELETE":
                boolean deleted = dataStore.delete(id);
                if (deleted) {
                    sendResponse(exchange, 204, "");
                } else {
                    sendResponse(exchange, 404, "Item not found");
                }
                break;
            default:
                sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length > 0 ? bytes.length : -1);
        try (OutputStream os = exchange.getResponseBody()) {
            if (bytes.length > 0) {
                os.write(bytes);
            }
        }
    }
}
