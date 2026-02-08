package com.example.shutdown;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShutdownDemo {

    private static final int PORT = 8080;
    private static HttpServer server;
    private static ExecutorService executor;
    // Track active requests for graceful shutdown
    private static final AtomicInteger activeRequests = new AtomicInteger(0);
    // Flag to indicate shutdown is in progress
    private static volatile boolean isShuttingDown = false;

    private static void log(String message) {
        System.out.println(java.time.LocalTime.now() + " " + message);
        System.out.flush();
    }

    public static void main(String[] args) throws IOException {
        // Create a thread pool for handling requests
        executor = Executors.newFixedThreadPool(10);

        // Create the HTTP server
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(executor);

        // Define a simple endpoint
        server.createContext("/", new HelloHandler());
        server.createContext("/long-process", new LongProcessHandler());

        // Register the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("\n[Shutdown Hook] Shutdown signal received (SIGINT/SIGTERM)...");
            stopServer();
        }));

        log("[Server] Starting server on port " + PORT);
        log("[Server] Press Ctrl+C to stop the server gracefully.");
        server.start();
    }

    private static void stopServer() {
        // Signal that we are shutting down
        isShuttingDown = true;
        log("[Shutdown Hook] Draining active requests...");

        // Wait for active requests to finish
        int maxWaitSeconds = 10;
        long endTime = System.currentTimeMillis() + (maxWaitSeconds * 1000);
        
        while (activeRequests.get() > 0 && System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log("[Shutdown Hook] Interrupted while waiting for requests to drain.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        if (activeRequests.get() > 0) {
            log("[Shutdown Hook] Timeout reached. There are still " + activeRequests.get() + " active requests.");
        } else {
            log("[Shutdown Hook] All active requests drained.");
        }

        log("[Shutdown Hook] Stopping HTTP server...");
        // Now stop the server immediately as we've already drained
        server.stop(0);
        
        log("[Shutdown Hook] Shutting down executor service...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log("[Shutdown Hook] Executor did not terminate in the specified time.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log("[Shutdown Hook] Interrupted during shutdown.");
            executor.shutdownNow();
        }
        
        log("[Shutdown Hook] Server stopped gracefully.");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (isShuttingDown) {
                exchange.sendResponseHeaders(503, -1);
                return;
            }
            activeRequests.incrementAndGet();
            try {
                String response = "Hello! Send a SIGINT (Ctrl+C) to test graceful shutdown.";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } finally {
                activeRequests.decrementAndGet();
            }
        }
    }

    static class LongProcessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (isShuttingDown) {
                exchange.sendResponseHeaders(503, -1);
                return;
            }
            activeRequests.incrementAndGet();
            try {
                log("[Handler] Starting long process...");
                try {
                    // Simulate a long task
                    Thread.sleep(5000); 
                } catch (InterruptedException e) {
                    log("[Handler] Long process interrupted!");
                }
                String response = "Long process finished!";
                log("[Handler] Long process finished. Sending response.");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } finally {
                activeRequests.decrementAndGet();
            }
        }
    }
}
