package com.example.filestorage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
    private final S3Client s3Client;
    private final S3Config s3Config;

    public FileStorageService(S3Client s3Client, S3Config s3Config) {
        this.s3Client = s3Client;
        this.s3Config = s3Config;
        
        // Ensure bucket exists
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(s3Config.getBucket()).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(s3Config.getBucket()).build());
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(s3Config.getBucket())
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
        
        return key;
    }

    public byte[] downloadFile(String key) throws IOException {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(s3Config.getBucket())
                        .key(key)
                        .build()
        ).readAllBytes();
    }
}
