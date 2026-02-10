package com.fabiankaraben.imageprocessing.controller;

import com.fabiankaraben.imageprocessing.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/process")
    public ResponseEntity<byte[]> processImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "filter", defaultValue = "grayscale") String filter) {
        try {
            byte[] processedImage = imageService.processImage(file, filter);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(processedImage.length);
            headers.setContentDispositionFormData("attachment", "processed_" + file.getOriginalFilename());
            
            return new ResponseEntity<>(processedImage, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/info")
    public ResponseEntity<String> getImageInfo(@RequestParam("file") MultipartFile file) {
        try {
            String info = imageService.getImageInfo(file);
            return ResponseEntity.ok(info);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid image");
        }
    }
}
