package com.example.middleware;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleLoggerMiddlewareTest {

    private HttpServer server;
    private int port;
    private TestLogHandler testLogHandler;
    private Logger logger;

    @BeforeEach
    void setUp() throws IOException {
        // Setup Logger
        logger = Logger.getLogger(SimpleLoggerMiddleware.class.getName());
        testLogHandler = new TestLogHandler();
        logger.addHandler(testLogHandler);

        // Setup Server
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/test", exchange -> {
            String response = "Test Response";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }).getFilters().add(new SimpleLoggerMiddleware());

        server.setExecutor(null);
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
        logger.removeHandler(testLogHandler);
    }

    @Test
    void testLoggerMiddleware() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/test"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify request was successful
        assertTrue(response.statusCode() == 200);

        // Verify log message
        boolean logFound = false;
        for (LogRecord record : testLogHandler.getLogRecords()) {
            String message = record.getMessage();
            // Expected format: "%s %s took %dms" -> "GET /test took ...ms"
            if (message.contains("GET") && message.contains("/test") && message.contains("took")) {
                logFound = true;
                break;
            }
        }
        assertTrue(logFound, "Log message confirming request details was not found.");
    }

    // Custom Handler to capture log records
    static class TestLogHandler extends Handler {
        private final List<LogRecord> logRecords = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            logRecords.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        public List<LogRecord> getLogRecords() {
            return logRecords;
        }
    }
}
