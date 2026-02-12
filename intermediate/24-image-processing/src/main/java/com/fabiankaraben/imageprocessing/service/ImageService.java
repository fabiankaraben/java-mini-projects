package com.fabiankaraben.imageprocessing.service;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageService {

    public byte[] processImage(MultipartFile file, String filter) throws IOException {
        // Load image using ImageIO (ImageJ can also load, but ImageIO is standard for MultipartFile -> BufferedImage)
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Failed to read image");
        }

        // Convert to ImageJ's ImagePlus
        ImagePlus imp = new ImagePlus("image", originalImage);
        ImageProcessor ip = imp.getProcessor();

        // Apply filter based on request
        switch (filter.toLowerCase()) {
            case "grayscale":
                // Convert to grayscale
                ImageProcessor grayIp = ip.convertToByte(true);
                imp.setProcessor(grayIp);
                break;
            case "blur":
                // Apply Gaussian blur
                ip.blurGaussian(2.0);
                break;
            case "edges":
                // Find edges
                ip.findEdges();
                break;
            case "invert":
                // Invert colors
                ip.invert();
                break;
            default:
                // No op
                break;
        }

        // Convert back to byte array (JPEG format for simplicity)
        BufferedImage processedImage = imp.getBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(processedImage, "png", baos);
        return baos.toByteArray();
    }
    
    public String getImageInfo(MultipartFile file) throws IOException {
         BufferedImage originalImage = ImageIO.read(file.getInputStream());
         if (originalImage == null) {
             throw new IOException("Failed to read image");
         }
         return String.format("Width: %d, Height: %d", originalImage.getWidth(), originalImage.getHeight());
    }
}
