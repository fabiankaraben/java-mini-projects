package com.example.oauth;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest
class OAuthServiceTest {

    private OAuthService oAuthService;
    private String tokenEndpoint;
    private final String clientId = "test-client-id";
    private final String clientSecret = "test-client-secret";
    private final String authEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String redirectUri = "http://localhost:7070/callback";

    @BeforeEach
    void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
        tokenEndpoint = wmRuntimeInfo.getHttpBaseUrl() + "/token";
        oAuthService = new OAuthService(clientId, clientSecret, authEndpoint, tokenEndpoint, redirectUri);
    }

    @Test
    void shouldGenerateCorrectAuthorizationUrl() {
        String url = oAuthService.getAuthorizationUrl();
        
        assertThat(url).startsWith(authEndpoint);
        assertThat(url).contains("client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8));
        assertThat(url).contains("redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        assertThat(url).contains("response_type=code");
        assertThat(url).contains("scope=email+profile"); // URLEncoder encodes space as +
    }

    @Test
    void shouldExchangeCodeForToken() throws IOException, InterruptedException {
        String code = "valid-auth-code";
        String accessToken = "mock-access-token";

        stubFor(post("/token")
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withRequestBody(containing("code=" + code))
                .withRequestBody(containing("grant_type=authorization_code"))
                .withRequestBody(containing("client_id=" + clientId))
                .withRequestBody(containing("client_secret=" + clientSecret))
                .withRequestBody(containing("redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)))
                .willReturn(okJson("{\"access_token\": \"" + accessToken + "\", \"token_type\": \"Bearer\", \"expires_in\": 3600}")));

        String result = oAuthService.exchangeCodeForToken(code);

        assertThat(result).isEqualTo(accessToken);
    }

    @Test
    void shouldThrowExceptionWhenTokenExchangeFails() {
        String code = "invalid-auth-code";

        stubFor(post("/token")
                .willReturn(badRequest().withBody("Invalid grant")));

        assertThatThrownBy(() -> oAuthService.exchangeCodeForToken(code))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to retrieve token");
    }
}
