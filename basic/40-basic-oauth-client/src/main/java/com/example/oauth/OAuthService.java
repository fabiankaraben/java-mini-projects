package com.example.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuthService {

    private final String clientId;
    private final String clientSecret;
    private final String authEndpoint;
    private final String tokenEndpoint;
    private final String redirectUri;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OAuthService(String clientId, String clientSecret, String authEndpoint, String tokenEndpoint, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authEndpoint = authEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.redirectUri = redirectUri;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getAuthorizationUrl() {
        return authEndpoint + "?response_type=code" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode("email profile", StandardCharsets.UTF_8);
    }

    public String exchangeCodeForToken(String code) throws IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", redirectUri);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);

        String form = params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to retrieve token: " + response.body());
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());
        if (jsonNode.has("access_token")) {
            return jsonNode.get("access_token").asText();
        } else {
             throw new RuntimeException("No access_token in response: " + response.body());
        }
    }
}
