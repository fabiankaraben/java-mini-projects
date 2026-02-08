package com.example;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CookieServerTest {

    private static HttpServer server;
    private static final int PORT = 8081; // Different port for testing
    private static final String BASE_URL = "http://localhost:" + PORT + "/";

    @BeforeAll
    static void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new CookieServer.CookieHandler());
        server.setExecutor(null);
        server.start();
    }

    @AfterAll
    static void tearDown() {
        server.stop(0);
    }

    @Test
    void testSetCookieOnFirstRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Cookie set! Reload to see it.", response.body());
        
        Optional<String> setCookieHeader = response.headers().firstValue("Set-Cookie");
        assertTrue(setCookieHeader.isPresent(), "Set-Cookie header should be present");
        assertTrue(setCookieHeader.get().startsWith("user_session="), "Cookie should start with user_session=");
        assertTrue(setCookieHeader.get().contains("Path=/"), "Cookie should have Path=/");
        assertTrue(setCookieHeader.get().contains("HttpOnly"), "Cookie should be HttpOnly");
    }

    @Test
    void testReadCookieOnSubsequentRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String cookieValue = "test-cookie-value-123";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Cookie", "user_session=" + cookieValue)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Welcome back! Found cookie: " + cookieValue, response.body());
    }

    @Test
    void testMultipleCookies() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String cookieValue = "complex-cookie-value";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Cookie", "other_cookie=abc; user_session=" + cookieValue + "; another=xyz")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Welcome back! Found cookie: " + cookieValue, response.body());
    }
}
