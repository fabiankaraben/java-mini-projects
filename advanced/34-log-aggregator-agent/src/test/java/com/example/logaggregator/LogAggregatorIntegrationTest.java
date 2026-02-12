package com.example.logaggregator;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@WireMockTest(httpPort = 8089)
class LogAggregatorIntegrationTest {

    @TempDir
    static Path tempDir;

    static File monitoredFile;

    @Autowired
    private LogFileTailer logFileTailer;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {
        monitoredFile = tempDir.resolve("app.log").toFile();
        // Create the file if it doesn't exist
        if (!monitoredFile.exists()) {
            monitoredFile.createNewFile();
        }
        
        registry.add("log.aggregator.file.path", monitoredFile::getAbsolutePath);
        registry.add("log.aggregator.server.url", () -> "http://localhost:8089/logs");
    }

    @BeforeEach
    void setUp() {
        stubFor(post(urlEqualTo("/logs"))
                .willReturn(aResponse().withStatus(200)));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clear file content for next test
        Files.write(monitoredFile.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void shouldTailFileAndSendLogsToServer() throws IOException {
        String logLine1 = "INFO: Application started";
        String logLine2 = "ERROR: Something went wrong";

        // Append lines to the file
        appendLine(logLine1);
        
        // Wait for the first log to be sent
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> 
            verify(postRequestedFor(urlEqualTo("/logs"))
                    .withRequestBody(containing(logLine1)))
        );

        appendLine(logLine2);

        // Wait for the second log to be sent
        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> 
            verify(postRequestedFor(urlEqualTo("/logs"))
                    .withRequestBody(containing(logLine2)))
        );
    }

    private void appendLine(String line) throws IOException {
        Files.write(monitoredFile.toPath(), (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        // Add a small delay to ensure file system modification time is updated/detected if needed by tailer (though Tailer is usually fast)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
