package com.example.gzip;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GzipServerTest {

    private static HttpServer server;
    private static final int PORT = 8081; // Use a different port for testing to avoid conflicts
    private static final String BASE_URL = "http://localhost:" + PORT + "/";

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new GzipServer.GzipHandler());
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void testGzipCompression() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept-Encoding", "gzip")
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Verify Status Code
        assertEquals(200, response.statusCode());

        // Verify Content-Encoding header
        String contentEncoding = response.headers().firstValue("Content-Encoding").orElse(null);
        assertEquals("gzip", contentEncoding, "Response should be GZIP compressed");

        // Verify Decompressed Body
        String decompressedBody = decompress(response.body());
        assertTrue(decompressedBody.startsWith("This is a simple text response"), "Decompressed body should match expected text");
    }

    @Test
    void testNoCompression() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept-Encoding", "identity") // or just omit it
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify Status Code
        assertEquals(200, response.statusCode());

        // Verify Content-Encoding header is NOT gzip
        String contentEncoding = response.headers().firstValue("Content-Encoding").orElse(null);
        assertNull(contentEncoding, "Response should NOT be GZIP compressed");

        // Verify Body
        assertTrue(response.body().startsWith("This is a simple text response"), "Body should match expected text");
    }

    private String decompress(byte[] compressed) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressed));
             InputStreamReader reader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8);
             BufferedReader buffered = new BufferedReader(reader)) {
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = buffered.readLine()) != null) {
                out.append(line);
            }
            return out.toString();
        }
    }
}
