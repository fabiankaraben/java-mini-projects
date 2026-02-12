package com.example.twofactorauth.controller;

import com.example.twofactorauth.model.TotpSecretResponse;
import com.example.twofactorauth.model.TotpValidationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TotpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGenerateAndValidateFlow() throws Exception {
        // 1. Generate Secret
        MvcResult generateResult = mockMvc.perform(post("/api/totp/generate")
                        .param("accountName", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secret").exists())
                .andExpect(jsonPath("$.qrCodeUrl").exists())
                .andReturn();

        String responseContent = generateResult.getResponse().getContentAsString();
        TotpSecretResponse secretResponse = objectMapper.readValue(responseContent, TotpSecretResponse.class);
        String secret = secretResponse.getSecret();

        // 2. Generate a valid code using the secret locally (acting as the client/authenticator app)
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        int code = gAuth.getTotpPassword(secret);

        // 3. Validate the code
        TotpValidationRequest validationRequest = new TotpValidationRequest(secret, code);

        mockMvc.perform(post("/api/totp/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }
    
    @Test
    public void testValidateInvalidCode() throws Exception {
        TotpValidationRequest validationRequest = new TotpValidationRequest("JBSWY3DPEHPK3PXP", 123456); // Arbitrary secret and code

        mockMvc.perform(post("/api/totp/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }
}
