package com.fabiankaraben.filedownload;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileDownloadServerTest {

    private HttpServer server;
    private static final int TEST_PORT = 8081;
    private static final String BASE_URL = "http://localhost:" + TEST_PORT + "/download";

    @BeforeEach
    void setUp() throws IOException {
        server = FileDownloadServer.startServer(TEST_PORT);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void testFileDownloadHeadersAndContent() throws IOException {
        String fileName = "sample.txt";
        URL url = new URL(BASE_URL + "/" + fileName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Response code should be 200 OK");

        // Verify Content-Disposition header
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        assertNotNull(contentDisposition, "Content-Disposition header should not be null");
        assertEquals("attachment; filename=\"" + fileName + "\"", contentDisposition, "Content-Disposition should match");

        // Verify Content-Type header
        String contentType = connection.getHeaderField("Content-Type");
        assertEquals("application/octet-stream", contentType, "Content-Type should be application/octet-stream");

        // Read response body
        byte[] downloadedBytes;
        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            downloadedBytes = out.toByteArray();
        }

        // Read original file
        File originalFile = new File("src/main/resources/files/" + fileName);
        assertTrue(originalFile.exists(), "Original file must exist for test");
        byte[] originalBytes = Files.readAllBytes(originalFile.toPath());

        // Compare byte arrays
        assertArrayEquals(originalBytes, downloadedBytes, "Downloaded content should match original file content");
    }

    @Test
    void testFileNotFound() throws IOException {
        URL url = new URL(BASE_URL + "/nonexistent.txt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(404, responseCode, "Response code should be 404 Not Found");
    }
}
