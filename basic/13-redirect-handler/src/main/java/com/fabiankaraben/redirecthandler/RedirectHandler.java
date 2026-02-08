package com.fabiankaraben.redirecthandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RedirectHandler implements HttpHandler {
    private final String redirectUrl;
    private final int statusCode; // 301 or 302

    public RedirectHandler(String redirectUrl, int statusCode) {
        this.redirectUrl = redirectUrl;
        this.statusCode = statusCode;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Set the Location header for redirection
        exchange.getResponseHeaders().set("Location", redirectUrl);
        
        // Send the response headers with the redirection status code
        // The second parameter is the response length. -1 means no response body.
        exchange.sendResponseHeaders(statusCode, -1);
    }
}
