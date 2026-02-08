package com.fabiankaraben.urlshortener;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Main {

    private static final UrlStore urlStore = new UrlStore();
    private static final String BASE_URL = "http://localhost:7070/";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        app.post("/shorten", Main::shortenUrl);
        app.get("/{id}", Main::redirectUrl);
    }

    // Helper for testing to stop the server
    public static Javalin startServer(int port) {
        Javalin app = Javalin.create().start(port);
        app.post("/shorten", Main::shortenUrl);
        app.get("/{id}", Main::redirectUrl);
        return app;
    }

    private static void shortenUrl(Context ctx) {
        ShortenRequest request = ctx.bodyAsClass(ShortenRequest.class);
        if (request.url == null || request.url.isEmpty()) {
            ctx.status(400).result("URL is required");
            return;
        }

        String id = urlStore.shorten(request.url);
        String shortUrl = BASE_URL + id; // Note: In a real app, base URL would be dynamic or config
        
        ctx.json(new ShortenResponse(id, shortUrl, request.url));
    }

    private static void redirectUrl(Context ctx) {
        String id = ctx.pathParam("id");
        String originalUrl = urlStore.getOriginalUrl(id);

        if (originalUrl != null) {
            ctx.redirect(originalUrl);
        } else {
            ctx.status(404).result("URL not found");
        }
    }

    // DTOs
    public static class ShortenRequest {
        public String url;
        // Default constructor for Jackson
        public ShortenRequest() {}
        public ShortenRequest(String url) { this.url = url; }
    }

    public static class ShortenResponse {
        public String id;
        public String shortUrl;
        public String originalUrl;

        public ShortenResponse(String id, String shortUrl, String originalUrl) {
            this.id = id;
            this.shortUrl = shortUrl;
            this.originalUrl = originalUrl;
        }
    }
}
