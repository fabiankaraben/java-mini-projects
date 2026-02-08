package com.example.oauth;

import io.javalin.http.Context;

public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public void login(Context ctx) {
        String authUrl = oAuthService.getAuthorizationUrl();
        ctx.redirect(authUrl);
    }

    public void callback(Context ctx) {
        String code = ctx.queryParam("code");
        if (code == null) {
            ctx.status(400).result("Missing 'code' query parameter");
            return;
        }

        try {
            String token = oAuthService.exchangeCodeForToken(code);
            ctx.json(new TokenResponse(token));
        } catch (Exception e) {
            ctx.status(500).result("Error exchanging code for token: " + e.getMessage());
        }
    }

    // Simple DTO for JSON response
    private static class TokenResponse {
        public final String accessToken;

        public TokenResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
