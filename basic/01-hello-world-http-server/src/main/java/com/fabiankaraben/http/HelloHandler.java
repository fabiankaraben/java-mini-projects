package com.fabiankaraben.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(HelloHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        logger.info(String.format("Received %s request for %s", method, path));

        if ("GET".equals(method)) {
            handleGetRequest(exchange);
        } else {
            handleMethodNotAllowed(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String response = "Hello World";
        sendResponse(exchange, 200, response);
    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "Method Not Allowed";
        sendResponse(exchange, 405, response);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing response", e);
            throw e;
        }
    }
}
