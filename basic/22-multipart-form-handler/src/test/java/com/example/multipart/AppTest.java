package com.example.multipart;

import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        app = new App().start(0);
        baseUrl = "http://localhost:" + app.port();
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
    }

    @Test
    public void testMultipartUpload() throws IOException {
        // Create a temporary file to upload
        File tempFile = File.createTempFile("test-upload", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Hello, Multipart!");
        }

        HttpResponse<JsonNode> response = Unirest.post(baseUrl + "/upload")
                .field("username", "testuser")
                .field("description", "Integration test upload")
                .field("file", tempFile)
                .asJson();

        assertThat(response.getStatus()).isEqualTo(200);
        
        JsonNode body = response.getBody();
        assertThat(body.getObject().getString("status")).isEqualTo("success");
        assertThat(body.getObject().getJSONObject("fields").getString("username")).isEqualTo("testuser");
        assertThat(body.getObject().getJSONObject("fields").getString("description")).isEqualTo("Integration test upload");
        
        // Check if file is in the list (name might vary slightly due to temp file naming)
        String firstFile = body.getObject().getJSONArray("files").getString(0);
        assertThat(firstFile).contains(tempFile.getName());
        
        // Clean up temp file
        tempFile.deleteOnExit();
    }
}
