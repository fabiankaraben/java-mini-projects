package com.example.videostreaming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VideoStreamingServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @TempDir
    static Path tempHlsDir;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.hls.dir", () -> tempHlsDir.toString());
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldServeHlsManifest() throws Exception {
        // Create a dummy HLS manifest file in the temporary directory
        Path manifestPath = tempHlsDir.resolve("test-stream.m3u8");
        Files.writeString(manifestPath, "#EXTM3U\n#EXT-X-VERSION:3\n#EXTINF:4.000,\nsegment0.ts");

        // Verify that the manifest is accessible via HTTP
        mockMvc.perform(get("/hls/test-stream.m3u8"))
                .andExpect(status().isOk())
                .andExpect(content().string("#EXTM3U\n#EXT-X-VERSION:3\n#EXTINF:4.000,\nsegment0.ts"));
    }
}
