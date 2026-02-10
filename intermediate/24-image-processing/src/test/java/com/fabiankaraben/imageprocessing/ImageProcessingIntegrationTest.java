package com.fabiankaraben.imageprocessing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageProcessingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadAndProcessImage() throws Exception {
        // Create a simple blue 10x10 image
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                img.setRGB(x, y, 0x0000FF); // Blue
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", baos.toByteArray());

        // Test grayscale filter
        byte[] responseBytes = mockMvc.perform(multipart("/api/images/process")
                .file(file)
                .param("filter", "grayscale"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andReturn().getResponse().getContentAsByteArray();

        // Verify the output image properties
        BufferedImage processedImg = ImageIO.read(new ByteArrayInputStream(responseBytes));
        assertNotNull(processedImg);
        assertEquals(10, processedImg.getWidth());
        assertEquals(10, processedImg.getHeight());
        
        // In grayscale, pure blue (0,0,255) becomes roughly 29 (0.114 * 255)
        // Note: ImageJ grayscale conversion might differ slightly based on formula, 
        // but it should definitely NOT be blue anymore. 
        // Let's check a pixel.
        int rgb = processedImg.getRGB(5, 5) & 0xFFFFFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        
        // Grayscale means R=G=B
        assertEquals(r, g, "Red and Green components should be equal in grayscale");
        assertEquals(g, b, "Green and Blue components should be equal in grayscale");
    }

    @Test
    public void testGetImageInfo() throws Exception {
        // Create a 20x30 image
        BufferedImage img = new BufferedImage(20, 30, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos); // Use JPG for variety
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", baos.toByteArray());

        mockMvc.perform(multipart("/api/images/info")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Width: 20, Height: 30"));
    }
}
