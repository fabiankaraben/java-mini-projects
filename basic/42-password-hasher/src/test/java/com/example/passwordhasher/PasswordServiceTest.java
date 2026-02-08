package com.example.passwordhasher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void testPasswordHashingAndVerification() {
        String password = "mySecretPassword";
        String hash = passwordService.hashPassword(password);

        assertNotNull(hash);
        assertNotEquals(password, hash);
        assertTrue(passwordService.verifyPassword(password, hash), "Password should match its hash");
    }

    @Test
    void testDifferentPasswordsDoNotMatch() {
        String password = "password123";
        String hash = passwordService.hashPassword(password);
        String wrongPassword = "password124";

        assertFalse(passwordService.verifyPassword(wrongPassword, hash), "Different password should not match hash");
    }

    @Test
    void testInvalidHash() {
        assertFalse(passwordService.verifyPassword("password", "invalidHash"), "Invalid hash format should return false");
    }
}
