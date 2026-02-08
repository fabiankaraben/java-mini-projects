package com.fabiankaraben.redirecthandler;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedirectHandlerTest {

    private HttpServer server;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        // Create a server on a random available port
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.setExecutor(null);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void test302FoundRedirect() throws IOException, InterruptedException {
        String redirectTarget = "https://www.example.com";
        server.createContext("/redirect", new RedirectHandler(redirectTarget, 302));

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER) // Do not follow redirects automatically
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/redirect"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(302, response.statusCode(), "Status code should be 302 Found");
        assertEquals(redirectTarget, response.headers().firstValue("Location").orElse(null), "Location header should match target");
    }

    @Test
    void test301MovedPermanentlyRedirect() throws IOException, InterruptedException {
        String redirectTarget = "https://www.example.com/new";
        server.createContext("/moved", new RedirectHandler(redirectTarget, 301));

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER) // Do not follow redirects automatically
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/moved"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(301, response.statusCode(), "Status code should be 301 Moved Permanently");
        assertEquals(redirectTarget, response.headers().firstValue("Location").orElse(null), "Location header should match target");
    }
}
