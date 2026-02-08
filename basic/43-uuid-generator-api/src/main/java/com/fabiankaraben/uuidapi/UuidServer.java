package com.fabiankaraben.uuidapi;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class UuidServer {

    private static final Logger logger = Logger.getLogger(UuidServer.class.getName());
    private static final int DEFAULT_PORT = 8080;
    private final int port;
    private HttpServer server;

    public UuidServer() {
        this(DEFAULT_PORT);
    }

    public UuidServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/uuid", new UuidHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        logger.info("UUID Generator API Server started on port " + getPort());
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    public int getPort() {
        if (server != null) {
            return server.getAddress().getPort();
        }
        return port;
    }

    public static void main(String[] args) {
        UuidServer server = new UuidServer();
        try {
            server.start();
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Stopping server...");
                server.stop();
            }));
            
        } catch (IOException e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }
}
