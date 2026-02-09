package com.fabiankaraben.fileupload;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUploadHandler implements HttpHandler {

    private static final Logger logger = Logger.getLogger(FileUploadHandler.class.getName());
    private final String uploadDir;

    public FileUploadHandler(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        if ("POST".equals(method)) {
            handlePostRequest(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            if (contentType == null || !contentType.startsWith("multipart/form-data")) {
                sendResponse(exchange, 415, "Unsupported Media Type. Expected multipart/form-data");
                return;
            }

            String boundary = extractBoundary(contentType);
            if (boundary == null) {
                sendResponse(exchange, 400, "Bad Request: Missing boundary");
                return;
            }

            // Simple multipart parsing
            // Note: This is a simplified parser for educational purposes. 
            // Production code should use a robust library like Apache Commons FileUpload.
            
            InputStream is = exchange.getRequestBody();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] bodyBytes = buffer.toByteArray();
            
            // Search for filename and content
            // We are looking for: Content-Disposition: form-data; name="file"; filename="example.txt"
            
            // Convert start of body to string to search for headers (limited length to avoid memory issues with huge files)
            // But here we already read everything into memory which is not ideal for large files, but acceptable for this mini-project.
            
            // String bodyString = new String(bodyBytes, StandardCharsets.ISO_8859_1); // ISO-8859-1 preserves byte values 1-to-1
            String boundaryMarker = "--" + boundary;
            
            // Split by boundary
            // We need to be careful with splitting because the file content might contain the boundary string (unlikely but possible if not constructed carefully)
            // However, for this simple implementation, we'll assume the parts are separated by the boundary.
            
            // Find the filename
            String filename = null;
            int startContent = -1;
            int endContent = -1;
            
            // Very manual parsing
            // 1. Find the first part
            int firstBoundaryIndex = indexOf(bodyBytes, boundaryMarker.getBytes(StandardCharsets.ISO_8859_1), 0);
            if (firstBoundaryIndex == -1) {
                sendResponse(exchange, 400, "Bad Request: No parts found");
                return;
            }
            
            int startPart = firstBoundaryIndex + boundaryMarker.length();
            
            // Find headers end (\r\n\r\n)
            byte[] headerEndMarker = "\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);
            int headerEndIndex = indexOf(bodyBytes, headerEndMarker, startPart);
            
            if (headerEndIndex != -1) {
                // Extract headers
                String headers = new String(bodyBytes, startPart, headerEndIndex - startPart, StandardCharsets.ISO_8859_1);
                
                // Extract filename
                if (headers.contains("filename=\"")) {
                    int filenameStart = headers.indexOf("filename=\"") + 10;
                    int filenameEnd = headers.indexOf("\"", filenameStart);
                    if (filenameEnd != -1) {
                        filename = headers.substring(filenameStart, filenameEnd);
                        // Clean filename (remove path if present)
                        Path p = Paths.get(filename);
                        filename = p.getFileName().toString();
                    }
                }
                
                startContent = headerEndIndex + 4; // Skip \r\n\r\n
                
                // Find end of this part (next boundary)
                // The next boundary starts with \r\n--boundary
                String nextBoundaryMarker = "\r\n--" + boundary;
                endContent = indexOf(bodyBytes, nextBoundaryMarker.getBytes(StandardCharsets.ISO_8859_1), startContent);
            }
            
            if (filename != null && startContent != -1 && endContent != -1) {
                Path filePath = Paths.get(uploadDir, filename);
                
                // Extract the actual file bytes
                int length = endContent - startContent;
                byte[] fileContent = new byte[length];
                System.arraycopy(bodyBytes, startContent, fileContent, 0, length);
                
                Files.write(filePath, fileContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                
                logger.info("File uploaded: " + filename);
                sendResponse(exchange, 200, "File uploaded successfully: " + filename);
            } else {
                sendResponse(exchange, 400, "Bad Request: Could not parse file part");
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing upload", e);
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }
    
    private int indexOf(byte[] outerArray, byte[] smallerArray, int startIndex) {
        for(int i = startIndex; i < outerArray.length - smallerArray.length + 1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    private String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring(9);
            }
        }
        return null;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
