package com.fabiankaraben.uuidapi;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UuidServerIntegrationTest {

    private static UuidServer server;
    private static HttpClient client;
    private static String baseUrl;
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @BeforeAll
    static void setUp() throws IOException {
        // Start server on random port
        server = new UuidServer(0);
        server.start();
        baseUrl = "http://localhost:" + server.getPort() + "/api/uuid";
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @Test
    void testUuidUniquenessAndFormat() throws IOException, InterruptedException {
        Set<String> uuids = new HashSet<>();
        int iterations = 10;

        for (int i = 0; i < iterations; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertEquals("application/json", response.headers().firstValue("Content-Type").orElse(""));

            String body = response.body();
            // Simple JSON parsing since we don't want to add Jackson dependency just for tests if not needed, 
            // but the prompt said "Dependency Manager: Maven" and I didn't see Jackson in my pom explicitly but I could add it or just parse string.
            // The response format is: {"uuid": "..."}
            
            assertTrue(body.contains("\"uuid\":"), "Response should contain uuid field");
            
            // Extract UUID string value
            String uuidStr = body.split("\"")[3]; 
            
            // Validate format
            assertTrue(UUID_PATTERN.matcher(uuidStr).matches(), "UUID should match standard format: " + uuidStr);
            
            // Ensure it is a valid UUID parsable by Java
            assertNotNull(UUID.fromString(uuidStr));

            uuids.add(uuidStr);
        }

        assertEquals(iterations, uuids.size(), "All generated UUIDs should be unique");
    }

    @Test
    void testMethodNotAllowed() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }
}
