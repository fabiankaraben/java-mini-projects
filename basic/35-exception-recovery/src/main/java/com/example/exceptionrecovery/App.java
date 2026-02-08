package com.example.exceptionrecovery;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Define endpoints
        server.createContext("/hello", new SafeHandler(new HelloHandler()));
        server.createContext("/poison", new SafeHandler(new PoisonHandler()));

        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    public static void stop(HttpServer server) {
        if (server != null) {
            server.stop(0);
        }
    }
    
    // For testing purposes, we expose a way to create and start the server
    public static HttpServer startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/hello", new SafeHandler(new HelloHandler()));
        server.createContext("/poison", new SafeHandler(new PoisonHandler()));
        server.setExecutor(null);
        server.start();
        return server;
    }
}

// Wrapper handler that catches RuntimeException
class SafeHandler implements HttpHandler {
    private final HttpHandler delegate;

    public SafeHandler(HttpHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            delegate.handle(exchange);
        } catch (RuntimeException e) {
            System.err.println("Recovered from unchecked exception: " + e.getMessage());
            e.printStackTrace();
            
            String response = "Internal Server Error: " + e.getMessage();
            exchange.sendResponseHeaders(500, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}

class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Hello, World!";
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

class PoisonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Simulate a crash
        throw new RuntimeException("This is a poison pill!");
    }
}
