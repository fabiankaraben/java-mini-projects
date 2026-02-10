package com.example.backgroundjobprocessor;

import com.example.backgroundjobprocessor.service.EmailService;
import org.jobrunr.storage.StorageProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class JobIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StorageProvider storageProvider;

    @Test
    void testJobEnqueueing() throws Exception {
        String recipient = "test@example.com";

        mockMvc.perform(post("/jobs/email")
                        .param("recipient", recipient))
                .andExpect(status().isOk())
                .andExpect(content().string("Job enqueued for recipient: " + recipient));

        // Wait for job to be processed (JobRunr processes asynchronously)
        await().atMost(Duration.ofSeconds(10)).until(() -> 
            storageProvider.getJobStats().getSucceeded() >= 1
        );
    }
}
