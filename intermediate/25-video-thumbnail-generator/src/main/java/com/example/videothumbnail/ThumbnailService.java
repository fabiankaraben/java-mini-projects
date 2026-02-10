package com.example.videothumbnail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ThumbnailService {

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    @Value("${thumbnail.dir:thumbnails}")
    private String thumbnailDir;

    private final FFmpegInvoker ffmpegInvoker;

    public ThumbnailService(FFmpegInvoker ffmpegInvoker) {
        this.ffmpegInvoker = ffmpegInvoker;
    }

    public String generateThumbnail(MultipartFile videoFile) throws IOException {
        // Ensure directories exist
        Files.createDirectories(Paths.get(uploadDir));
        Files.createDirectories(Paths.get(thumbnailDir));

        String videoFileName = UUID.randomUUID().toString() + "_" + videoFile.getOriginalFilename();
        Path videoPath = Paths.get(uploadDir, videoFileName);
        videoFile.transferTo(videoPath);

        String thumbnailFileName = UUID.randomUUID().toString() + ".png";
        Path thumbnailPath = Paths.get(thumbnailDir, thumbnailFileName);

        ffmpegInvoker.generateThumbnail(videoPath, thumbnailPath);

        // Clean up video file to save space? Optional. For now let's keep it or delete it.
        // Files.delete(videoPath); 

        return thumbnailPath.toAbsolutePath().toString();
    }
    
    public byte[] getThumbnailData(String thumbnailPath) throws IOException {
        return Files.readAllBytes(Paths.get(thumbnailPath));
    }
}
