package com.example.webhook.model;

import java.time.Instant;
import java.util.UUID;

public record WebhookEvent(String id, String eventType, Object payload, Instant timestamp) {
    public WebhookEvent(String eventType, Object payload) {
        this(UUID.randomUUID().toString(), eventType, payload, Instant.now());
    }
}
