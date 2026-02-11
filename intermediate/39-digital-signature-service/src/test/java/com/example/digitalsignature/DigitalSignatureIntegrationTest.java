package com.example.digitalsignature;

import com.example.digitalsignature.model.KeyPairResponse;
import com.example.digitalsignature.model.SignRequest;
import com.example.digitalsignature.model.SignResponse;
import com.example.digitalsignature.model.VerifyRequest;
import com.example.digitalsignature.model.VerifyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DigitalSignatureIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFullFlow() {
        String baseUrl = "http://localhost:" + port + "/api/signature";

        // 1. Generate Keys
        ResponseEntity<KeyPairResponse> keysResponse = restTemplate.postForEntity(baseUrl + "/generate-keys", null, KeyPairResponse.class);
        assertEquals(HttpStatus.OK, keysResponse.getStatusCode());
        KeyPairResponse keys = keysResponse.getBody();
        assertNotNull(keys);
        assertNotNull(keys.getPublicKey());
        assertNotNull(keys.getPrivateKey());

        // 2. Sign Data
        String data = "Hello, World!";
        SignRequest signRequest = new SignRequest();
        signRequest.setData(data);
        signRequest.setPrivateKey(keys.getPrivateKey());

        ResponseEntity<SignResponse> signResponse = restTemplate.postForEntity(baseUrl + "/sign", signRequest, SignResponse.class);
        assertEquals(HttpStatus.OK, signResponse.getStatusCode());
        SignResponse signature = signResponse.getBody();
        assertNotNull(signature);
        assertNotNull(signature.getSignature());

        // 3. Verify Signature
        VerifyRequest verifyRequest = new VerifyRequest();
        verifyRequest.setData(data);
        verifyRequest.setSignature(signature.getSignature());
        verifyRequest.setPublicKey(keys.getPublicKey());

        ResponseEntity<VerifyResponse> verifyResponse = restTemplate.postForEntity(baseUrl + "/verify", verifyRequest, VerifyResponse.class);
        assertEquals(HttpStatus.OK, verifyResponse.getStatusCode());
        VerifyResponse verification = verifyResponse.getBody();
        assertNotNull(verification);
        assertTrue(verification.isValid());
    }
}
