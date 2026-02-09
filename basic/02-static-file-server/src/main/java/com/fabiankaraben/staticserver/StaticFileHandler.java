package com.fabiankaraben.staticserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticFileHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(StaticFileHandler.class.getName());
    private static final String BASE_PATH = "public";
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("txt", "text/plain");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        logger.info(String.format("Received %s request for %s", method, path));

        if (!"GET".equals(method)) {
            sendResponse(exchange, 405, "Method Not Allowed", "text/plain");
            return;
        }

        if ("/".equals(path)) {
            path = "/index.html";
        }

        // Prevent directory traversal attacks
        if (path.contains("..")) {
            sendResponse(exchange, 403, "Forbidden", "text/plain");
            return;
        }

        String resourcePath = BASE_PATH + path;
        URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);

        if (resourceUrl == null) {
            sendResponse(exchange, 404, "Not Found", "text/plain");
            return;
        }

        try {
            // Check if it's a file and readable
            // Since we are loading from classpath, URL check handles existence. 
            // We might want to check if it is a directory if we were serving from FS directly,
            // but ClassLoader.getResource usually returns null for directories unless they are packages? 
            // Actually it can return directory URLs. Let's be careful.
            
            // Basic check: if it ends with /, it might be a directory request which we mapped to index.html already if it was root.
            // If it's a specific folder request without trailing slash, implementation varies.
            // For simplicity, we assume we are serving files.
            
            byte[] fileBytes = loadResource(resourcePath);
            String contentType = getContentType(path);
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error serving file", e);
            sendResponse(exchange, 500, "Internal Server Error", "text/plain");
        }
    }

    private byte[] loadResource(String resourcePath) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return is.readAllBytes();
        }
    }

    private String getContentType(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < path.length() - 1) {
            String extension = path.substring(dotIndex + 1).toLowerCase();
            return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
        }
        return "application/octet-stream";
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        byte[] responseBytes = response.getBytes();
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
