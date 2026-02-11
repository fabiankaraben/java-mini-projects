package com.example.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileEncryptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEncryptionAndDecryptionFlow() throws Exception {
        // 1. Prepare a file to upload
        String originalContent = "This is a secret message that needs encryption.";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "secret.txt",
                MediaType.TEXT_PLAIN_VALUE,
                originalContent.getBytes()
        );

        // 2. Upload and Encrypt
        MvcResult encryptResult = mockMvc.perform(multipart("/api/files/encrypt").file(file))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = encryptResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseContent, Map.class);

        String encryptedData = (String) responseMap.get("encryptedData");
        String key = (String) responseMap.get("key");

        assertNotNull(encryptedData);
        assertNotNull(key);

        // 3. Decrypt
        Map<String, String> decryptRequest = Map.of(
                "encryptedData", encryptedData,
                "key", key
        );

        MvcResult decryptResult = mockMvc.perform(post("/api/files/decrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decryptRequest)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] decryptedBytes = decryptResult.getResponse().getContentAsByteArray();
        String decryptedContent = new String(decryptedBytes);

        // 4. Verify content matches
        assertArrayEquals(originalContent.getBytes(), decryptedBytes);
    }
}
