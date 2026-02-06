package com.fabiankaraben.queryparser;

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
        server.createContext("/api/parse", new QueryHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        logger.info("Query Parameter Parser Server started on port " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    public static void main(String[] args) {
        SimpleHttpServer server = new SimpleHttpServer();
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
    
    public int getPort() {
        if (server != null) {
            return server.getAddress().getPort();
        }
        return PORT;
    }
}
