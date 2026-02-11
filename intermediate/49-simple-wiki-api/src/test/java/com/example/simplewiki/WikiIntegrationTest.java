package com.example.simplewiki;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WikiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPageVersioningFlow() throws Exception {
        String slug = "my-test-page";
        String title = "My Test Page";
        String contentV1 = "This is the first version.";
        String contentV2 = "This is the second version (updated).";
        String contentV3 = "This is the third version (final).";

        // 1. Create Page (Version 1)
        mockMvc.perform(post("/api/pages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of(
                        "slug", slug,
                        "title", title,
                        "content", contentV1
                ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug", is(slug)))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.versions", hasSize(1)));

        // 2. Update Page (Version 2)
        mockMvc.perform(put("/api/pages/" + slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of(
                        "content", contentV2
                ))))
                .andExpect(status().isOk());

        // 3. Update Page (Version 3)
        mockMvc.perform(put("/api/pages/" + slug)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(java.util.Map.of(
                        "content", contentV3
                ))))
                .andExpect(status().isOk());

        // 4. Verify Latest Content
        mockMvc.perform(get("/api/pages/" + slug + "/content"))
                .andExpect(status().isOk())
                .andExpect(content().string(contentV3));

        // 5. Verify History Integrity
        mockMvc.perform(get("/api/pages/" + slug + "/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].versionNumber", is(3))) // Latest first
                .andExpect(jsonPath("$[0].content", is(contentV3)))
                .andExpect(jsonPath("$[1].versionNumber", is(2)))
                .andExpect(jsonPath("$[1].content", is(contentV2)))
                .andExpect(jsonPath("$[2].versionNumber", is(1)))
                .andExpect(jsonPath("$[2].content", is(contentV1)));

        // 6. Verify Specific Versions
        mockMvc.perform(get("/api/pages/" + slug + "/versions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(contentV1));

        mockMvc.perform(get("/api/pages/" + slug + "/versions/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(contentV2));
    }
}
