package com.fabiankaraben.p2p.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

@Service
public class PeerClient {

    private static final Logger log = LoggerFactory.getLogger(PeerClient.class);

    private final FileService fileService;

    public PeerClient(FileService fileService) {
        this.fileService = fileService;
    }

    public void downloadFile(String peerHost, int peerPort, String fileName) {
        log.info("Connecting to peer {}:{} to download {}", peerHost, peerPort, fileName);
        try (Socket socket = new Socket(peerHost, peerPort);
             InputStream in = socket.getInputStream();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Send request
            out.println("GET " + fileName);

            // Read response header
            String header = readLine(in);
            log.debug("Received header: {}", header);

            if (header != null && header.startsWith("OK")) {
                // Parse size if needed, currently just logging it
                // String[] parts = header.split(" ");
                // long size = Long.parseLong(parts[1]);
                
                // Read content
                fileService.saveFile(fileName, in);
                log.info("File {} downloaded successfully.", fileName);
            } else {
                log.error("Peer responded with error: {}", header);
                throw new RuntimeException("Peer error: " + header);
            }

        } catch (IOException e) {
            log.error("Error downloading file from peer", e);
            throw new RuntimeException("Download failed", e);
        }
    }

    // Helper to read a line byte-by-byte to avoid over-buffering
    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\n') break;
            sb.append((char) b);
        }
        if (sb.length() == 0 && b == -1) return null;
        return sb.toString().trim();
    }
}
