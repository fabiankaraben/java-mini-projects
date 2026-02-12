package com.example.logaggregator;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;

@Component
public class LogFileTailer {

    private static final Logger logger = LoggerFactory.getLogger(LogFileTailer.class);

    private final String filePath;
    private final LogPublisher logPublisher;
    private Tailer tailer;
    private Thread tailerThread;

    public LogFileTailer(@Value("${log.aggregator.file.path}") String filePath, LogPublisher logPublisher) {
        this.filePath = filePath;
        this.logPublisher = logPublisher;
    }

    @PostConstruct
    public void start() {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.warn("File {} does not exist. Waiting for it to be created...", filePath);
        }

        TailerListenerAdapter listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                logPublisher.publish(line);
            }

            @Override
            public void handle(Exception ex) {
                logger.error("Error tailing file", ex);
            }
        };

        tailer = Tailer.builder()
                .setFile(file)
                .setTailerListener(listener)
                .setDelayDuration(Duration.ofMillis(1000))
                .setTailFromEnd(true)
                .get();

        tailerThread = new Thread(tailer);
        tailerThread.setDaemon(true);
        tailerThread.start();
        
        logger.info("Started tailing file: {}", filePath);
    }

    @PreDestroy
    public void stop() {
        if (tailer != null) {
            tailer.stop();
        }
        if (tailerThread != null) {
            try {
                tailerThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Stopped tailing file.");
    }
}
