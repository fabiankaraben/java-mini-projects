package com.example.zerotrust;

import com.example.zerotrust.dto.AuthRequest;
import com.example.zerotrust.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ZeroTrustApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginSuccess() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "user123");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginFailure() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "wrongpassword");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedResourceWithoutToken() throws Exception {
        mockMvc.perform(get("/api/resource/public"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedResourceWithValidToken() throws Exception {
        String token = obtainAccessToken("user", "user123");

        mockMvc.perform(get("/api/resource/public")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a public resource (authenticated users only)"));
    }

    @Test
    void testAdminAccessAdminResource() throws Exception {
        String token = obtainAccessToken("admin", "admin123");

        mockMvc.perform(get("/api/resource/admin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a restricted resource for ADMINs only"));
    }

    @Test
    void testUserAccessAdminResource() throws Exception {
        String token = obtainAccessToken("user", "user123");

        mockMvc.perform(get("/api/resource/admin")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGuestAccessUserResource() throws Exception {
        // Guest has ROLE_GUEST, but /api/resource/user requires USER or ADMIN
        String token = obtainAccessToken("guest", "guest123");

        mockMvc.perform(get("/api/resource/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void testUserAccessUserResource() throws Exception {
        String token = obtainAccessToken("user", "user123");

        mockMvc.perform(get("/api/resource/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a restricted resource for USERs and ADMINs"));
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        AuthRequest authRequest = new AuthRequest(username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthResponse response = objectMapper.readValue(content, AuthResponse.class);
        return response.getToken();
    }
}
