package com.fabiankaraben.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SimpleHttpServer {

    private static final Logger logger = Logger.getLogger(SimpleHttpServer.class.getName());
    private static final int PORT = 8080;
    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HelloHandler());
        server.setExecutor(Executors.newCachedThreadPool()); // Use a thread pool
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
        SimpleHttpServer httpServer = new SimpleHttpServer();
        try {
            httpServer.start();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Stopping server...");
                httpServer.stop();
            }));
            
        } catch (IOException e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }
    
    public int getPort() {
        if (server != null) {
            return server.getAddress().getPort();
        }
        return PORT;
    }
}
