package com.fabiankaraben.basicauth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class BasicAuthServer {

    private static final Logger logger = Logger.getLogger(BasicAuthServer.class.getName());
    private static final int PORT = 8080;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";
    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/protected", new ProtectedHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        logger.info("Server started on port " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    public static void main(String[] args) {
        BasicAuthServer server = new BasicAuthServer();
        try {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        } catch (IOException e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }

    static class ProtectedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                try {
                    String base64Credentials = authHeader.substring("Basic ".length());
                    String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
                    String[] parts = credentials.split(":", 2);

                    if (parts.length == 2 && USERNAME.equals(parts[0]) && PASSWORD.equals(parts[1])) {
                        sendResponse(exchange, 200, "Authentication successful! Welcome to the protected area.");
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    // Invalid Base64, treat as unauthorized
                }
            }

            // Authentication failed or missing
            exchange.getResponseHeaders().set("WWW-Authenticate", "Basic realm=\"Protected Area\"");
            sendResponse(exchange, 401, "Unauthorized");
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}
