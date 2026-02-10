package com.fabiankaraben.djl;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InferenceController.class)
public class InferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InferenceService inferenceService;

    @Test
    public void testClassifyImage() throws Exception {
        // Mock prediction result
        Classifications.Classification classification1 = new Classifications.Classification("tabby", 0.9);
        Classifications.Classification classification2 = new Classifications.Classification("tiger_cat", 0.05);

        given(inferenceService.predict(any(InputStream.class)))
                .willReturn(Arrays.asList(classification1, classification2));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/api/inference/classify").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].className").value("tabby"))
                .andExpect(jsonPath("$[0].probability").value(0.9))
                .andExpect(jsonPath("$[1].className").value("tiger_cat"))
                .andExpect(jsonPath("$[1].probability").value(0.05));
    }

    @Test
    public void testClassifyImageEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart("/api/inference/classify").file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testClassifyImageServiceError() throws Exception {
        given(inferenceService.predict(any(InputStream.class)))
                .willThrow(new TranslateException("Inference failed"));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/api/inference/classify").file(file))
                .andExpect(status().isInternalServerError());
    }
}
