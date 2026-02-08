package com.example.queue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class QueueServer {
    private static final Logger logger = LoggerFactory.getLogger(QueueServer.class);
    private final int port;
    private final QueueManager queueManager;
    private HttpServer server;

    public QueueServer(int port, QueueManager queueManager) {
        this.port = port;
        this.queueManager = queueManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/task", new TaskHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        logger.info("Server started on port {}", port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    private class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String content = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (content.isEmpty()) {
                    sendResponse(exchange, 400, "Task content cannot be empty");
                    return;
                }

                Task task = new Task(content);
                queueManager.submit(task);
                logger.info("Task submitted: {}", task.getId());

                String response = "Task submitted with ID: " + task.getId();
                sendResponse(exchange, 202, response);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
