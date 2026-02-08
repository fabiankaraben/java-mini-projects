package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new TemplateHandler());
        server.setExecutor(null);
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class TemplateHandler implements HttpHandler {
        private final TemplateRenderer renderer = new TemplateRenderer();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            try {
                Map<String, String> data = new HashMap<>();
                data.put("title", "My Mini Project");
                data.put("name", "User");
                data.put("message", "Welcome to basic template rendering in Java!");
                data.put("timestamp", LocalDateTime.now().toString());

                String response = renderer.render("template.html", data);

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                byte[] responseBytes = response.getBytes("UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        }
    }
}
