package com.example.configyaml;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Config from YAML Loader...");

        try {
            // Load from resources folder
            AppConfig config = ConfigLoader.loadConfigFromResources("config.yaml");
            
            System.out.println("Configuration loaded successfully!");
            System.out.println("App Name: " + config.getAppName());
            System.out.println("Port: " + config.getPort());

            // Start HTTP Server
            startServer(config);
            
        } catch (IOException e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void startServer(AppConfig config) throws IOException {
        int port = config.getPort();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Context to return the config itself as JSON
        server.createContext("/config", new ConfigHandler(config));
        
        server.setExecutor(null); // creates a default executor
        server.start();
        
        System.out.println("Server started on port " + port);
        System.out.println("Try: curl http://localhost:" + port + "/config");
    }

    static class ConfigHandler implements HttpHandler {
        private final AppConfig config;
        private final ObjectMapper mapper = new ObjectMapper();

        public ConfigHandler(AppConfig config) {
            this.config = config;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(200, responseBytes.length);
            
            try (OutputStream os = t.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}
