package com.fabiankaraben.imageresizer;

import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {

    private static Javalin app;
    private static int PORT;

    @BeforeAll
    public static void startServer() {
        app = App.getApp().start(0);
        PORT = app.port();
    }

    @AfterAll
    public static void stopServer() {
        app.stop();
    }

    @Test
    public void testImageResize() throws IOException {
        // Create a temporary test image
        int originalWidth = 100;
        int originalHeight = 100;
        BufferedImage originalImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_RGB);
        File tempFile = File.createTempFile("test-image", ".png");
        ImageIO.write(originalImage, "png", tempFile);

        int targetWidth = 50;
        int targetHeight = 50;

        HttpResponse<byte[]> response = Unirest.post("http://localhost:" + PORT + "/resize")
                .field("image", tempFile)
                .queryString("width", targetWidth)
                .queryString("height", targetHeight)
                .asBytes();

        assertEquals(200, response.getStatus());

        ByteArrayInputStream bais = new ByteArrayInputStream(response.getBody());
        BufferedImage resizedImage = ImageIO.read(bais);

        assertNotNull(resizedImage);
        assertEquals(targetWidth, resizedImage.getWidth());
        assertEquals(targetHeight, resizedImage.getHeight());

        // Cleanup
        tempFile.delete();
    }
}
