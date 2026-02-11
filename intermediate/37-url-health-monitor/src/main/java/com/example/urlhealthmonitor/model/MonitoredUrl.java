package com.example.urlhealthmonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MonitoredUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    private LocalDateTime createdAt;
    
    // Status from last check
    private String lastStatus;
    private int lastStatusCode;
    private LocalDateTime lastCheckedAt;

    public MonitoredUrl() {}

    public MonitoredUrl(String url) {
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getLastStatus() { return lastStatus; }
    public void setLastStatus(String lastStatus) { this.lastStatus = lastStatus; }
    public int getLastStatusCode() { return lastStatusCode; }
    public void setLastStatusCode(int lastStatusCode) { this.lastStatusCode = lastStatusCode; }
    public LocalDateTime getLastCheckedAt() { return lastCheckedAt; }
    public void setLastCheckedAt(LocalDateTime lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }
}
