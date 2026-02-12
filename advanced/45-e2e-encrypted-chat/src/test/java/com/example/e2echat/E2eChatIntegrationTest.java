package com.example.e2echat;

import com.example.e2echat.model.EncryptedMessage;
import com.example.e2echat.model.UserKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class E2eChatIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testEndToEndEncryptionFlow() throws Exception {
        // 1. Setup Alice and Bob Key Pairs (Diffie-Hellman)
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        
        KeyPair aliceKeyPair = keyPairGen.generateKeyPair();
        KeyPair bobKeyPair = keyPairGen.generateKeyPair();

        // 2. Register Public Keys
        String alicePubKeyBase64 = Base64.getEncoder().encodeToString(aliceKeyPair.getPublic().getEncoded());
        String bobPubKeyBase64 = Base64.getEncoder().encodeToString(bobKeyPair.getPublic().getEncoded());

        mockMvc.perform(post("/api/keys/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserKey("alice", alicePubKeyBase64))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/keys/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserKey("bob", bobPubKeyBase64))))
                .andExpect(status().isOk());

        // 3. Exchange Keys (Simulate Client Logic)
        // Alice fetches Bob's key
        MvcResult bobKeyResult = mockMvc.perform(get("/api/keys/bob"))
                .andExpect(status().isOk())
                .andReturn();
        String fetchedBobKeyBase64 = bobKeyResult.getResponse().getContentAsString();
        PublicKey fetchedBobKey = KeyFactory.getInstance("DH").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(fetchedBobKeyBase64)));

        // Bob fetches Alice's key
        MvcResult aliceKeyResult = mockMvc.perform(get("/api/keys/alice"))
                .andExpect(status().isOk())
                .andReturn();
        String fetchedAliceKeyBase64 = aliceKeyResult.getResponse().getContentAsString();
        PublicKey fetchedAliceKey = KeyFactory.getInstance("DH").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(fetchedAliceKeyBase64)));

        // 4. Compute Shared Secrets
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKeyPair.getPrivate());
        aliceKeyAgree.doPhase(fetchedBobKey, true);
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();

        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKeyPair.getPrivate());
        bobKeyAgree.doPhase(fetchedAliceKey, true);
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();

        // Use first 16 bytes for AES Key
        SecretKeySpec aliceAesKey = new SecretKeySpec(aliceSharedSecret, 0, 16, "AES");
        SecretKeySpec bobAesKey = new SecretKeySpec(bobSharedSecret, 0, 16, "AES");

        // 5. Alice Encrypts and Sends Message
        String originalMessage = "Hello Bob, this is a secret!";
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aliceAesKey);
        byte[] encryptedBytes = cipher.doFinal(originalMessage.getBytes());
        String encryptedContentBase64 = Base64.getEncoder().encodeToString(encryptedBytes);

        EncryptedMessage msgToSend = new EncryptedMessage("alice", "bob", encryptedContentBase64, 0);
        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(msgToSend)))
                .andExpect(status().isOk());

        // 6. Bob Receives and Decrypts Message
        MvcResult messagesResult = mockMvc.perform(get("/api/chat/messages").param("username", "bob"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = messagesResult.getResponse().getContentAsString();
        EncryptedMessage[] messages = objectMapper.readValue(responseContent, EncryptedMessage[].class);
        
        assertEquals(1, messages.length);
        String receivedEncryptedContent = messages[0].getContent();

        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, bobAesKey);
        byte[] decryptedBytes = decryptCipher.doFinal(Base64.getDecoder().decode(receivedEncryptedContent));
        String decryptedMessage = new String(decryptedBytes);

        assertEquals(originalMessage, decryptedMessage);
    }
}
