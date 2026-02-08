package com.fabiankaraben.filedownload;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class FileDownloadServer {

    private static final int PORT = 8080;
    private static final String FILES_DIR = "src/main/resources/files";

    public static void main(String[] args) throws IOException {
        startServer(PORT);
        System.out.println("Server started on port " + PORT);
        System.out.println("Try downloading: curl -v -O -J http://localhost:" + PORT + "/download/sample.txt");
    }

    public static HttpServer startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/download", new FileHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        return server;
    }

    static class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            
            if (fileName.isEmpty()) {
                sendResponse(exchange, 400, "Bad Request: No file specified");
                return;
            }

            File file = new File(FILES_DIR, fileName);
            if (!file.exists() || !file.isFile()) {
                sendResponse(exchange, 404, "File Not Found");
                return;
            }

            // Set headers
            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            
            // Send file content
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            byte[] bytes = message.getBytes();
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
