package com.example.authserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void performOidcLoginFlow() throws Exception {
        // 1. Initiate Authorization Request
        MockHttpSession session = new MockHttpSession();
        // Use manual URL to ensure query params are correctly parsed by the filter
        // Using single scope 'openid' to avoid encoding issues for now
        String authorizeUrl = "/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid&state=some-state&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client";
        
        MvcResult authorizeResult = mockMvc.perform(get(authorizeUrl)
                        .session(session)
                        .header(HttpHeaders.ACCEPT, "text/html")) // Ensure we get a login page redirect
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"))
                .andReturn();

        // 2. Perform Login
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "password")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        // Redirect goes back to /oauth2/authorize (saved request)
        String location = loginResult.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        // 3. Follow redirect to /oauth2/authorize
        // Since consent is disabled, this should redirect immediately to the client with the code
        MvcResult codeResult = mockMvc.perform(get(location)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        String clientRedirectUrl = codeResult.getResponse().getHeader("Location");
        assertThat(clientRedirectUrl).startsWith("http://127.0.0.1:8080/login/oauth2/code/oidc-client");
        assertThat(clientRedirectUrl).contains("code=");
        assertThat(clientRedirectUrl).contains("state=some-state");

        String code = extractParam(clientRedirectUrl, "code");
        assertThat(code).isNotNull();

        // 4. Exchange Code for Token
        mockMvc.perform(post("/oauth2/token")
                        .param("grant_type", "authorization_code")
                        .param("code", code)
                        .param("redirect_uri", "http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                        .header(HttpHeaders.AUTHORIZATION, "Basic b2lkYy1jbGllbnQ6c2VjcmV0")) // client_id:client_secret (oidc-client:secret) base64 encoded
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andExpect(jsonPath("$.id_token").isNotEmpty())
                .andExpect(jsonPath("$.token_type").value("Bearer"));
    }
    
    private String extractParam(String url, String param) {
        Pattern pattern = Pattern.compile("[?&]" + param + "=([^&]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
