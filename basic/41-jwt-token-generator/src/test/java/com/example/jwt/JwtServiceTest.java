package com.example.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    public void testGenerateAndValidateToken() {
        String subject = "testUser";
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        claims.put("id", 123);

        // Generate Token
        String token = jwtService.generateToken(subject, claims);
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());

        System.out.println("Generated Token: " + token);

        // Validate Token
        Claims parsedClaims = jwtService.validateToken(token);
        
        Assertions.assertEquals(subject, parsedClaims.getSubject());
        Assertions.assertEquals("admin", parsedClaims.get("role"));
        Assertions.assertEquals(123, parsedClaims.get("id"));
    }
}
