package com.example.transcoding;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;
import ws.schild.jave.Encoder;

import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TranscodingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TranscodingService transcodingService;

    private final Path uploadDir = Paths.get("uploads");
    private final Path outputDir = Paths.get("outputs");

    @AfterEach
    public void cleanup() throws Exception {
        FileSystemUtils.deleteRecursively(uploadDir);
        FileSystemUtils.deleteRecursively(outputDir);
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public TranscodingService testTranscodingService() {
            return new TranscodingService() {
                @Override
                protected Encoder getEncoder() {
                    Encoder mockEncoder = Mockito.mock(Encoder.class);
                    try {
                        Mockito.doAnswer(invocation -> {
                            File target = invocation.getArgument(1);
                            // Simulate transcoding by writing dummy content to the target file
                            if (target != null) {
                                Files.createDirectories(target.toPath().getParent());
                                Files.write(target.toPath(), "transcoded content".getBytes());
                            }
                            return null;
                        }).when(mockEncoder).encode(any(MultimediaObject.class), any(File.class), any(EncodingAttributes.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return mockEncoder;
                }
            };
        }
    }

    @Test
    public void testVideoUploadAndTranscoding() throws Exception {
        // 1. Upload
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-video.mov",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "dummy video content".getBytes()
        );

        String responseJson = mockMvc.perform(multipart("/api/transcode").file(file))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        String jobId = responseJson.split("\"id\":\"")[1].split("\"")[0];

        // 2. Wait for completion
        await().atMost(Duration.ofSeconds(10)).until(() -> {
            TranscodingJob job = transcodingService.getJob(jobId);
            return job != null && job.getStatus() == JobStatus.COMPLETED;
        });

        // 3. Verify status endpoint
        mockMvc.perform(get("/api/transcode/" + jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.outputFilename").value(jobId + ".mp4"));

        // 4. Verify output file exists
        File outputFile = outputDir.resolve(jobId + ".mp4").toFile();
        assert outputFile.exists();
        assert outputFile.length() > 0;
        
        // 5. Verify download
        mockMvc.perform(get("/api/transcode/" + jobId + "/download"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("video/mp4")))
                .andExpect(content().bytes("transcoded content".getBytes()));
    }
}
