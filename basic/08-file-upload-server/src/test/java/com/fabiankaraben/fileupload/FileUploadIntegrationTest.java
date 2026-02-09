package com.fabiankaraben.fileupload;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUploadIntegrationTest {

    private FileUploadServer server;
    private static final String TEST_UPLOAD_DIR = "uploads";
    private static final String TEST_FILE_CONTENT = "This is a test file content for upload.";

    @BeforeEach
    void setUp() throws IOException {
        // clean up uploads dir before test
        deleteUploadsDir();
        
        server = new FileUploadServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.stop();
        deleteUploadsDir();
    }
    
    private void deleteUploadsDir() throws IOException {
        Path path = Paths.get(TEST_UPLOAD_DIR);
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            }
        }
    }

    @Test
    void testFileUpload() throws IOException {
        // Create a temporary file to upload
        File tempFile = File.createTempFile("upload-test", ".txt");
        Files.write(tempFile.toPath(), TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8));
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost("http://localhost:" + server.getPort() + "/api/upload");
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            // Important: The handler expects the part name to verify, but our simple handler extracts filename from any part.
            // Standardizing on 'file' as the form field name.
            builder.addPart("file", new FileBody(tempFile, ContentType.DEFAULT_TEXT));
            
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            
            int code = httpClient.execute(uploadFile, response -> response.getCode());
            assertEquals(200, code);
        }
        
        // Verify file exists in uploads directory
        Path uploadedFilePath = Paths.get(TEST_UPLOAD_DIR, tempFile.getName());
        assertTrue(Files.exists(uploadedFilePath), "Uploaded file should exist");
        
        // Verify content
        String uploadedContent = new String(Files.readAllBytes(uploadedFilePath), StandardCharsets.UTF_8);
        assertEquals(TEST_FILE_CONTENT, uploadedContent, "Uploaded content should match");
        
        // Cleanup temp file
        tempFile.delete();
    }
}
