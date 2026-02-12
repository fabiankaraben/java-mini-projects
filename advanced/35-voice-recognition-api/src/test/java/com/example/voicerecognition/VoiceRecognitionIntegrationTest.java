package com.example.voicerecognition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoiceRecognitionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoiceRecognitionService voiceRecognitionService;

    @Test
    public void testRecognizeAudio() throws Exception {
        // Mock the service behavior to avoid needing the actual Vosk model during tests
        String expectedJson = "{ \"text\" : \"hello world\" }";
        when(voiceRecognitionService.recognize(any(InputStream.class))).thenReturn(expectedJson);

        // Create a dummy audio file (content doesn't matter since we mock the service)
        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test.wav",
                "audio/wav",
                new byte[]{1, 2, 3, 4}
        );

        mockMvc.perform(multipart("/api/recognize")
                        .file(audioFile))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }
}
