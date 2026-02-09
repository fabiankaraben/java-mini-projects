package com.example.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testLogGeneration() {
        // Get the Logger for the controller
        Logger logger = (Logger) LoggerFactory.getLogger(LogController.class);

        // Create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // Add the appender to the logger
        logger.addAppender(listAppender);

        try {
            // Call the endpoint
            String message = "IntegrationTestMessage";
            String url = "http://localhost:" + port + "/log?level=INFO&message=" + message;
            restTemplate.getForObject(url, String.class);

            // Verify the log was captured
            assertThat(listAppender.list)
                    .extracting(ILoggingEvent::getFormattedMessage)
                    .anyMatch(msg -> msg.contains("INFO Log: " + message));

            assertThat(listAppender.list)
                    .extracting(ILoggingEvent::getLevel)
                    .contains(Level.INFO);

        } finally {
            // Detach the appender to avoid side effects
            logger.detachAppender(listAppender);
        }
    }
}
