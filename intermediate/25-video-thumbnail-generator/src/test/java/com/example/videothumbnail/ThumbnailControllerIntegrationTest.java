package com.example.videothumbnail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ThumbnailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FFmpegInvoker ffmpegInvoker;

    @Test
    void shouldUploadVideoAndReturnThumbnail() throws Exception {
        // Mock the ffmpeg invoker to simulate creating a thumbnail file
        doAnswer(invocation -> {
            Path thumbnailPath = invocation.getArgument(1);
            // Write some dummy bytes to simulate a thumbnail image
            Files.write(thumbnailPath, new byte[]{1, 2, 3, 4, 5});
            return null;
        }).when(ffmpegInvoker).generateThumbnail(any(Path.class), any(Path.class));

        MockMultipartFile videoFile = new MockMultipartFile(
                "file",
                "test-video.mp4",
                "video/mp4",
                "dummy video content".getBytes()
        );

        mockMvc.perform(multipart("/api/videos/thumbnail").file(videoFile))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"))
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(new byte[]{1, 2, 3, 4, 5}));
    }
}
