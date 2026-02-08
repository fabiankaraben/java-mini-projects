package com.fabiankaraben.imageresizer;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class App {

    public static void main(String[] args) {
        getApp().start(7070);
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.requestLogger.http((ctx, ms) -> {
                System.out.println(ctx.method() + " " + ctx.path() + " took " + ms + " ms");
            });
        });

        app.post("/resize", App::handleResize);

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).result("Internal Server Error: " + e.getMessage());
        });

        return app;
    }

    private static void handleResize(Context ctx) {
        UploadedFile uploadedFile = ctx.uploadedFile("image");
        if (uploadedFile == null) {
            ctx.status(400).result("Image file is missing. Please upload a file with key 'image'");
            return;
        }

        String widthParam = ctx.queryParam("width");
        String heightParam = ctx.queryParam("height");

        if (widthParam == null || heightParam == null) {
            ctx.status(400).result("Width and height query parameters are required");
            return;
        }

        int width;
        int height;
        try {
            width = Integer.parseInt(widthParam);
            height = Integer.parseInt(heightParam);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Width and height must be integers");
            return;
        }

        try {
            BufferedImage originalImage = ImageIO.read(uploadedFile.content());
            if (originalImage == null) {
                ctx.status(400).result("Uploaded file is not a valid image");
                return;
            }

            BufferedImage resizedImage = resizeImage(originalImage, width, height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Default to returning png for simplicity and quality, or try to keep original format
            String extension = uploadedFile.extension().replace(".", "");
            if (extension.isEmpty()) extension = "png";
            
            // ImageIO doesn't support all extensions equally well, let's stick to standard ones or default to png
            if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("jpeg") && !extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("gif")) {
                 extension = "png";
            }

            ImageIO.write(resizedImage, extension, baos);
            byte[] imageBytes = baos.toByteArray();

            ctx.contentType(uploadedFile.contentType() != null ? uploadedFile.contentType() : "image/" + extension);
            ctx.result(imageBytes);

        } catch (IOException e) {
            ctx.status(500).result("Failed to process image: " + e.getMessage());
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        
        // Preserve transparency for PNG if possible, but TYPE_INT_RGB doesn't support alpha.
        // Let's check if original has alpha
        if (originalImage.getColorModel().hasAlpha()) {
            outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
}
