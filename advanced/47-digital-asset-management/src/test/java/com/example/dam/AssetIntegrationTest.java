package com.example.dam;

import com.example.dam.repository.AssetRepository;
import com.example.dam.service.MetadataExtractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AssetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssetRepository assetRepository;

    @MockBean
    private MetadataExtractionService metadataExtractionService;

    @BeforeEach
    void setUp() {
        assetRepository.deleteAll();
    }

    @Test
    void testUploadAndSearchByMetadata() throws Exception {
        // Mock Metadata Extraction
        Map<String, String> mockMetadata = new HashMap<>();
        mockMetadata.put("Exif - Artist", "Test Artist");
        mockMetadata.put("Exif - Make", "Test Camera");
        given(metadataExtractionService.extractMetadata(any())).willReturn(mockMetadata);

        // Upload Asset
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/api/assets").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.filename", is("test-image.jpg")))
                .andExpect(jsonPath("$.version", is(1)))
                .andExpect(jsonPath("$.metadata['Exif - Artist']", is("Test Artist")));

        // Upload a second version (same filename)
        mockMvc.perform(multipart("/api/assets").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.filename", is("test-image.jpg")))
                .andExpect(jsonPath("$.version", is(2)));

        // Search by Metadata
        mockMvc.perform(get("/api/assets/search")
                        .param("key", "Exif - Artist")
                        .param("value", "Test Artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Both versions match

        mockMvc.perform(get("/api/assets/search")
                        .param("key", "Exif - Make")
                        .param("value", "Non Existent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
