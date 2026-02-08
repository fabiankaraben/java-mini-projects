package com.example.gzip;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class GzipServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new GzipHandler());
        server.setExecutor(null);
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class GzipHandler implements HttpHandler {
        private static final String RESPONSE_TEXT = "This is a simple text response that will be compressed if the client supports GZIP. " +
                "GZIP compression helps reduce the size of the response body, making data transfer more efficient over the network. " +
                "Repeating text to make it long enough to see compression benefits: " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String acceptEncoding = exchange.getRequestHeaders().getFirst("Accept-Encoding");
            boolean useGzip = acceptEncoding != null && acceptEncoding.contains("gzip");

            byte[] responseBytes;
            if (useGzip) {
                responseBytes = compress(RESPONSE_TEXT);
                exchange.getResponseHeaders().set("Content-Encoding", "gzip");
            } else {
                responseBytes = RESPONSE_TEXT.getBytes(StandardCharsets.UTF_8);
            }
            
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, responseBytes.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        private byte[] compress(String data) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
