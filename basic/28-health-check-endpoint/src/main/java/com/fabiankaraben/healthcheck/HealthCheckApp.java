package com.fabiankaraben.healthcheck;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HealthCheckApp {
    private HttpServer server;
    private static final int PORT = 8080;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/health", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "{\"status\": \"OK\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    public static void main(String[] args) throws IOException {
        HealthCheckApp app = new HealthCheckApp();
        app.start();
    }
}
