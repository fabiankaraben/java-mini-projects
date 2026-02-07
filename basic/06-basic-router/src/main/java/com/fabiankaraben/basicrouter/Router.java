package com.fabiankaraben.basicrouter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router implements HttpHandler {
    private final Map<String, Map<String, HttpHandler>> routes = new HashMap<>();

    public void addRoute(String method, String path, HttpHandler handler) {
        routes.computeIfAbsent(path, k -> new HashMap<>()).put(method, handler);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (routes.containsKey(path)) {
            Map<String, HttpHandler> methodHandlers = routes.get(path);
            if (methodHandlers.containsKey(method)) {
                methodHandlers.get(method).handle(exchange);
                return;
            } else {
                sendError(exchange, 405, "Method Not Allowed");
                return;
            }
        }

        sendError(exchange, 404, "Not Found");
    }

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        exchange.sendResponseHeaders(code, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }
}
