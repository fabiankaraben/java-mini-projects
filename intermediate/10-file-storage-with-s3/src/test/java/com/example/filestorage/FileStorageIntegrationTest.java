package com.example.filestorage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileStorageIntegrationTest {

    @Container
    private static final MinIOContainer minio = new MinIOContainer("minio/minio:latest")
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("s3.endpoint", minio::getS3URL);
        registry.add("s3.access-key", minio::getUserName);
        registry.add("s3.secret-key", minio::getPassword);
        registry.add("s3.bucket", () -> "test-bucket");
        registry.add("s3.region", () -> "us-east-1");
    }

    @Test
    void shouldUploadAndDownloadFile() throws IOException {
        // 1. Prepare file to upload
        String filename = "test-file.txt";
        String content = "Hello, S3!";
        ByteArrayResource resource = new ByteArrayResource(content.getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 2. Upload file
        String uploadUrl = "http://localhost:" + port + "/api/files/upload";
        ResponseEntity<String> uploadResponse = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        assertThat(uploadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String key = uploadResponse.getBody();
        assertThat(key).isNotNull();

        // 3. Download file
        String downloadUrl = "http://localhost:" + port + "/api/files/download/" + key;
        ResponseEntity<byte[]> downloadResponse = restTemplate.getForEntity(downloadUrl, byte[].class);

        assertThat(downloadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(downloadResponse.getBody()).isEqualTo(content.getBytes());
        assertThat(downloadResponse.getHeaders().getContentDisposition().getFilename()).isEqualTo(key);
    }
}
