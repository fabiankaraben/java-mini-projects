package com.fabiankaraben.fileupload;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class FileUploadServer {

    private static final Logger logger = Logger.getLogger(FileUploadServer.class.getName());
    private static final int PORT = 8080;
    private static final String UPLOAD_DIR = "uploads";
    private HttpServer server;

    public void start() throws IOException {
        // Ensure upload directory exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created upload directory: " + uploadPath.toAbsolutePath());
        }

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/upload", new FileUploadHandler(UPLOAD_DIR));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        logger.info("File Upload Server started on port " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("Server stopped");
        }
    }

    public static void main(String[] args) {
        FileUploadServer server = new FileUploadServer();
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
