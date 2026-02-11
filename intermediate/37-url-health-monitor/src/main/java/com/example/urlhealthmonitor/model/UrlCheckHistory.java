package com.example.urlhealthmonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UrlCheckHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monitored_url_id")
    private Long monitoredUrlId;

    private int statusCode;
    private String status; // UP/DOWN
    private LocalDateTime checkedAt;
    private long responseTimeMs;
    private String errorMessage;

    public UrlCheckHistory() {}

    public UrlCheckHistory(Long monitoredUrlId, int statusCode, String status, LocalDateTime checkedAt, long responseTimeMs, String errorMessage) {
        this.monitoredUrlId = monitoredUrlId;
        this.statusCode = statusCode;
        this.status = status;
        this.checkedAt = checkedAt;
        this.responseTimeMs = responseTimeMs;
        this.errorMessage = errorMessage;
    }

    public Long getId() { return id; }
    public Long getMonitoredUrlId() { return monitoredUrlId; }
    public int getStatusCode() { return statusCode; }
    public String getStatus() { return status; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public String getErrorMessage() { return errorMessage; }
}
