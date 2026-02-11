package com.example.urlhealthmonitor.service;

import com.example.urlhealthmonitor.model.MonitoredUrl;
import com.example.urlhealthmonitor.model.UrlCheckHistory;
import com.example.urlhealthmonitor.repository.MonitoredUrlRepository;
import com.example.urlhealthmonitor.repository.UrlCheckHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(UrlMonitorService.class);

    private final MonitoredUrlRepository urlRepository;
    private final UrlCheckHistoryRepository historyRepository;
    private final WebClient webClient;

    public UrlMonitorService(MonitoredUrlRepository urlRepository, UrlCheckHistoryRepository historyRepository) {
        this.urlRepository = urlRepository;
        this.historyRepository = historyRepository;
        this.webClient = WebClient.builder().build();
    }

    public MonitoredUrl addUrl(String url) {
        // Simple normalization/validation could go here
        return urlRepository.save(new MonitoredUrl(url));
    }

    public List<MonitoredUrl> getAllUrls() {
        return urlRepository.findAll();
    }

    public List<UrlCheckHistory> getHistory(Long urlId) {
        return historyRepository.findByMonitoredUrlIdOrderByCheckedAtDesc(urlId);
    }

    @Scheduled(fixedRateString = "${monitor.schedule.rate:60000}") // Default 1 minute
    public void checkAllUrls() {
        logger.info("Starting scheduled URL health check...");
        List<MonitoredUrl> urls = urlRepository.findAll();
        
        for (MonitoredUrl url : urls) {
            checkUrl(url);
        }
        logger.info("Finished URL health check.");
    }

    private void checkUrl(MonitoredUrl url) {
        LocalDateTime startTime = LocalDateTime.now();
        long startMillis = System.currentTimeMillis();
        
        try {
            webClient.get()
                    .uri(url.getUrl())
                    .retrieve()
                    .toEntity(String.class)
                    .subscribe(
                        response -> handleSuccess(url, response, startTime, startMillis),
                        error -> handleError(url, error, startTime, startMillis)
                    );
        } catch (Exception e) {
            // Synchronous error catch just in case, though subscribe handles async
            handleError(url, e, startTime, startMillis);
        }
    }

    private void handleSuccess(MonitoredUrl url, ResponseEntity<String> response, LocalDateTime startTime, long startMillis) {
        long duration = System.currentTimeMillis() - startMillis;
        HttpStatusCode status = response.getStatusCode();
        
        recordResult(url, status.value(), "UP", startTime, duration, null);
    }

    private void handleError(MonitoredUrl url, Throwable error, LocalDateTime startTime, long startMillis) {
        long duration = System.currentTimeMillis() - startMillis;
        // Simple error classification
        int statusCode = 0;
        String errorMessage = error.getMessage();
        
        if (error instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
            statusCode = ((org.springframework.web.reactive.function.client.WebClientResponseException) error).getStatusCode().value();
        }

        recordResult(url, statusCode, "DOWN", startTime, duration, errorMessage);
    }

    private void recordResult(MonitoredUrl url, int statusCode, String status, LocalDateTime checkedAt, long duration, String errorMessage) {
        // Update URL status
        url.setLastStatus(status);
        url.setLastStatusCode(statusCode);
        url.setLastCheckedAt(checkedAt);
        urlRepository.save(url);

        // Add history record
        UrlCheckHistory history = new UrlCheckHistory(
            url.getId(),
            statusCode,
            status,
            checkedAt,
            duration,
            errorMessage
        );
        historyRepository.save(history);
        
        logger.info("Checked {}: {} ({}ms) - {}", url.getUrl(), status, duration, statusCode);
    }
}
