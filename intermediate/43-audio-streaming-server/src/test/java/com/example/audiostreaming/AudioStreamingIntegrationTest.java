package com.example.audiostreaming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AudioStreamingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFullContent() throws Exception {
        mockMvc.perform(get("/api/audio/test.mp3"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(content().string("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"));
    }

    @Test
    public void testPartialContentFirst10Bytes() throws Exception {
        mockMvc.perform(get("/api/audio/test.mp3")
                        .header("Range", "bytes=0-9"))
                .andExpect(status().isPartialContent())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().string("Content-Range", "bytes 0-9/37"))
                .andExpect(content().string("0123456789"));
    }

    @Test
    public void testPartialContentMiddle() throws Exception {
        mockMvc.perform(get("/api/audio/test.mp3")
                        .header("Range", "bytes=10-19"))
                .andExpect(status().isPartialContent())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().string("Content-Range", "bytes 10-19/37"))
                .andExpect(content().string("ABCDEFGHIJ"));
    }

    @Test
    public void testPartialContentEnd() throws Exception {
        // Total length is 37 (including newline)
        // Request last 7 bytes: 30-36
        mockMvc.perform(get("/api/audio/test.mp3")
                        .header("Range", "bytes=30-36"))
                .andExpect(status().isPartialContent())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().string("Content-Range", "bytes 30-36/37"))
                .andExpect(content().string("UVWXYZ\n"));
    }
    
    @Test
    public void testRangeNotSatisfiable() throws Exception {
        mockMvc.perform(get("/api/audio/test.mp3")
                        .header("Range", "bytes=100-200"))
                .andExpect(status().isRequestedRangeNotSatisfiable());
    }

    @Test
    public void testFileNotFound() throws Exception {
        mockMvc.perform(get("/api/audio/nonexistent.mp3"))
                .andExpect(status().isNotFound());
    }
}
