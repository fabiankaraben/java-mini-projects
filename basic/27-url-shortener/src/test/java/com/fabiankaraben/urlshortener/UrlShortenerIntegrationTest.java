package com.fabiankaraben.urlshortener;

import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlShortenerIntegrationTest {

    private static Javalin app;
    private static final int PORT = 7070;
    private static final String BASE_URL = "http://localhost:" + PORT;

    @BeforeAll
    public static void startServer() {
        app = Main.startServer(PORT);
    }

    @AfterAll
    public static void stopServer() {
        app.stop();
    }

    @Test
    public void testUrlShorteningFlow() {
        String originalUrl = "https://www.example.com/some/long/path";

        // 1. Create short URL
        HttpResponse<JsonNode> shortenResponse = Unirest.post(BASE_URL + "/shorten")
                .body(new JSONObject().put("url", originalUrl))
                .asJson();

        assertEquals(200, shortenResponse.getStatus());
        JSONObject jsonResponse = shortenResponse.getBody().getObject();
        
        String id = jsonResponse.getString("id");
        String shortUrl = jsonResponse.getString("shortUrl");
        String returnedOriginalUrl = jsonResponse.getString("originalUrl");

        assertNotNull(id);
        assertTrue(shortUrl.endsWith(id));
        assertEquals(originalUrl, returnedOriginalUrl);

        // 2. Access short URL -> Verify Redirect to original
        // Disable redirect following to assert the 302/303 status and Location header.
        Unirest.config().reset();
        Unirest.config().followRedirects(false);

        HttpResponse<String> manualRedirectResponse = Unirest.get(BASE_URL + "/" + id).asString();
        
        assertEquals(302, manualRedirectResponse.getStatus());
        assertEquals(originalUrl, manualRedirectResponse.getHeaders().getFirst("Location"));
        
        // Reset config
        Unirest.config().reset();
    }

    @Test
    public void testNotFound() {
        HttpResponse<String> response = Unirest.get(BASE_URL + "/nonexistent").asString();
        assertEquals(404, response.getStatus());
        assertEquals("URL not found", response.getBody());
    }

    @Test
    public void testInvalidRequest() {
        HttpResponse<String> response = Unirest.post(BASE_URL + "/shorten")
                .body("{}")
                .asString();
        
        assertEquals(400, response.getStatus());
        assertEquals("URL is required", response.getBody());
    }
}
