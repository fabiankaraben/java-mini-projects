package com.example.digitalsignature.service;

import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import static org.junit.jupiter.api.Assertions.*;

class DigitalSignatureServiceTest {

    private final DigitalSignatureService digitalSignatureService = new DigitalSignatureService();

    @Test
    void testSignAndVerify() throws Exception {
        // Generate keys
        KeyPair keyPair = digitalSignatureService.generateKeyPair();
        String publicKey = digitalSignatureService.encodeKey(keyPair.getPublic());
        String privateKey = digitalSignatureService.encodeKey(keyPair.getPrivate());

        String data = "This is a secret message";

        // Sign data
        String signature = digitalSignatureService.sign(data, privateKey);
        assertNotNull(signature);

        // Verify signature
        boolean isValid = digitalSignatureService.verify(data, signature, publicKey);
        assertTrue(isValid);
    }

    @Test
    void testVerifyInvalidSignature() throws Exception {
        // Generate keys
        KeyPair keyPair = digitalSignatureService.generateKeyPair();
        String publicKey = digitalSignatureService.encodeKey(keyPair.getPublic());
        String privateKey = digitalSignatureService.encodeKey(keyPair.getPrivate());

        String data = "This is a secret message";
        String otherData = "This is a modified message";

        // Sign data
        String signature = digitalSignatureService.sign(data, privateKey);

        // Verify with different data
        boolean isValid = digitalSignatureService.verify(otherData, signature, publicKey);
        assertFalse(isValid);
    }
}
