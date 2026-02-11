package com.example.webhook.model;

import java.util.UUID;

public record WebhookSubscription(String id, String url, String eventType) {
    public WebhookSubscription(String url, String eventType) {
        this(UUID.randomUUID().toString(), url, eventType);
    }
}
