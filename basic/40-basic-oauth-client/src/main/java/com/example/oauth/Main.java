package com.example.oauth;

import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {
        // Configuration - in a real app, load from env vars or config file
        String clientId = System.getenv().getOrDefault("OAUTH_CLIENT_ID", "test-client-id");
        String clientSecret = System.getenv().getOrDefault("OAUTH_CLIENT_SECRET", "test-client-secret");
        String authEndpoint = System.getenv().getOrDefault("OAUTH_AUTH_ENDPOINT", "https://accounts.google.com/o/oauth2/v2/auth");
        String tokenEndpoint = System.getenv().getOrDefault("OAUTH_TOKEN_ENDPOINT", "https://oauth2.googleapis.com/token");
        String redirectUri = System.getenv().getOrDefault("OAUTH_REDIRECT_URI", "http://localhost:7070/callback");

        OAuthService oAuthService = new OAuthService(clientId, clientSecret, authEndpoint, tokenEndpoint, redirectUri);
        OAuthController oAuthController = new OAuthController(oAuthService);

        Javalin app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.result("Welcome to Basic OAuth Client! Go to /login to start."));
        app.get("/login", oAuthController::login);
        app.get("/callback", oAuthController::callback);
    }
}
