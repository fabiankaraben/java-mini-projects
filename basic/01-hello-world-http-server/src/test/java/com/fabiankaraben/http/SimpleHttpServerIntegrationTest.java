package com.fabiankaraben.http;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleHttpServerIntegrationTest {

    private static SimpleHttpServer server;
    private static HttpClient client;
    private static String baseUrl;

    @BeforeAll
    static void setUp() throws IOException {
        server = new SimpleHttpServer();
        server.start();
        int port = server.getPort();
        baseUrl = "http://localhost:" + port + "/";
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @Test
    void testGetEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Hello World", response.body());
    }

    @Test
    void testPostEndpoint_MethodNotAllowed() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Method Not Allowed", response.body());
    }
}
