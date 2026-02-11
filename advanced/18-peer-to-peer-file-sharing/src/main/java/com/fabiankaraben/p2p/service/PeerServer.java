package com.fabiankaraben.p2p.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PeerServer {

    private static final Logger log = LoggerFactory.getLogger(PeerServer.class);

    private final int port;
    private final FileService fileService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    public PeerServer(@Value("${p2p.server.port:8081}") int port, FileService fileService) {
        this.port = port;
        this.fileService = fileService;
    }

    @PostConstruct
    public void start() {
        executorService.submit(() -> {
            try {
                serverSocket = new ServerSocket(port);
                running = true;
                log.info("PeerServer started on port {}", port);

                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        executorService.submit(() -> handleClient(clientSocket));
                    } catch (IOException e) {
                        if (running) {
                            log.error("Error accepting connection", e);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Could not start PeerServer", e);
            }
        });
    }

    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error closing server socket", e);
        }
        executorService.shutdownNow();
    }

    private void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            String request = in.readLine();
            log.info("Received request: {}", request);

            if (request != null && request.startsWith("GET ")) {
                String fileName = request.substring(4).trim();
                try {
                    File file = fileService.getFile(fileName);
                    long length = file.length();
                    out.writeBytes("OK " + length + "\n");
                    
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                    out.flush();
                    log.info("File {} sent successfully", fileName);
                } catch (Exception e) {
                    log.error("Error serving file {}", fileName, e);
                    out.writeBytes("ERROR " + e.getMessage() + "\n");
                }
            } else {
                out.writeBytes("ERROR Invalid Request\n");
            }

        } catch (IOException e) {
            log.error("Error handling client connection", e);
        }
    }
}
