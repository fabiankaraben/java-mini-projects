package com.example.jwt.security;

import com.example.jwt.model.Role;
import com.example.jwt.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpiration", 86400000L); // 1 day
        ReflectionTestUtils.setField(jwtUtils, "refreshExpiration", 604800000L); // 7 days
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        User user = new User("testuser", "password", Role.USER);
        String token = jwtUtils.generateToken(user);

        assertNotNull(token);
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testTokenValidation() {
        User user = new User("validUser", "password", Role.USER);
        String token = jwtUtils.generateToken(user);

        assertTrue(jwtUtils.isTokenValid(token, user));
    }

    @Test
    void testTokenValidationWithDifferentUser() {
        User user = new User("user1", "password", Role.USER);
        User otherUser = new User("user2", "password", Role.USER);
        String token = jwtUtils.generateToken(user);

        assertFalse(jwtUtils.isTokenValid(token, otherUser));
    }
}
