package com.example.transcoding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/transcode")
public class TranscodingController {

    private final TranscodingService transcodingService;
    private final Path uploadDir = Paths.get("uploads");
    private final Path outputDir = Paths.get("outputs");

    @Autowired
    public TranscodingController(TranscodingService transcodingService) {
        this.transcodingService = transcodingService;
        try {
            Files.createDirectories(uploadDir);
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @PostMapping
    public ResponseEntity<TranscodingJob> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String id = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        TranscodingJob job = transcodingService.createJob(id, originalFilename);

        try {
            String extension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                extension = originalFilename.substring(i);
            }
            
            File sourceFile = uploadDir.resolve(id + extension).toFile();
            file.transferTo(sourceFile);

            File targetFile = outputDir.resolve(id + ".mp4").toFile();
            transcodingService.transcode(id, sourceFile, targetFile);

            return ResponseEntity.accepted().body(job);
        } catch (IOException e) {
            job.setStatus(JobStatus.FAILED);
            job.setMessage("Failed to store uploaded file");
            return ResponseEntity.internalServerError().body(job);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranscodingJob> getJobStatus(@PathVariable String id) {
        TranscodingJob job = transcodingService.getJob(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String id) {
        TranscodingJob job = transcodingService.getJob(id);
        if (job == null || job.getStatus() != JobStatus.COMPLETED) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = outputDir.resolve(job.getOutputFilename());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("video/mp4"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
