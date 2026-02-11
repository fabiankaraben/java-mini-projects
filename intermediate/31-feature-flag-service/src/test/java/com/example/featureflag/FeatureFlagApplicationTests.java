package com.example.featureflag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeatureFlagApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeatureFlagService featureFlagService;

    @Test
    void testDefaultFlags() throws Exception {
        // Verify default flags
        mockMvc.perform(get("/api/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['new-feature']", is(false)))
                .andExpect(jsonPath("$.['beta-ui']", is(true)));
    }

    @Test
    void testEnableDisableFeature() throws Exception {
        String featureName = "test-feature";

        // Initially, it should be disabled (default)
        mockMvc.perform(get("/api/features/" + featureName))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // Enable the feature
        mockMvc.perform(post("/api/features/" + featureName))
                .andExpect(status().isOk());

        // Verify it is enabled
        mockMvc.perform(get("/api/features/" + featureName))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Disable the feature
        mockMvc.perform(delete("/api/features/" + featureName))
                .andExpect(status().isOk());

        // Verify it is disabled
        mockMvc.perform(get("/api/features/" + featureName))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testDemoEndpointBehaviorChange() throws Exception {
        String featureName = "new-message-feature";

        // Ensure feature is disabled initially
        featureFlagService.setFeature(featureName, false);

        // Check demo endpoint (Old behavior)
        mockMvc.perform(get("/api/demo/message"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OLD feature")));

        // Enable feature
        featureFlagService.setFeature(featureName, true);

        // Check demo endpoint (New behavior)
        mockMvc.perform(get("/api/demo/message"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("NEW feature")));
    }
}
