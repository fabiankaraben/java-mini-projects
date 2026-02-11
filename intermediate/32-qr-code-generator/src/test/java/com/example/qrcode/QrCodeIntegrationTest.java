package com.example.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QrCodeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateAndDecodeQrCode() throws Exception {
        String textToEncode = "Hello, World! This is a test QR code.";

        byte[] imageBytes = mockMvc.perform(get("/api/qrcode")
                        .param("text", textToEncode)
                        .param("width", "400")
                        .param("height", "400"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        // Decode the generated QR code
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);

        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

        Result result = new MultiFormatReader().decode(binaryBitmap);

        assertEquals(textToEncode, result.getText());
    }
}
