package com.example.webhook.service;

import com.example.webhook.model.WebhookEvent;
import com.example.webhook.model.WebhookSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private final Map<String, List<WebhookSubscription>> subscriptions = new ConcurrentHashMap<>();
    private final WebClient webClient;

    public WebhookService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public WebhookSubscription registerSubscription(String url, String eventType) {
        WebhookSubscription subscription = new WebhookSubscription(url, eventType);
        subscriptions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(subscription);
        logger.info("Registered webhook: {} for event type: {}", url, eventType);
        return subscription;
    }

    public void triggerEvent(String eventType, Object payload) {
        WebhookEvent event = new WebhookEvent(eventType, payload);
        List<WebhookSubscription> subs = subscriptions.getOrDefault(eventType, List.of());
        
        logger.info("Triggering event {} for {} subscribers", event.id(), subs.size());

        for (WebhookSubscription sub : subs) {
            deliverPayload(sub, event);
        }
    }

    @Async
    protected void deliverPayload(WebhookSubscription sub, WebhookEvent event) {
        logger.info("Delivering event {} to {}", event.id(), sub.url());
        
        webClient.post()
                .uri(sub.url())
                .bodyValue(event)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> logger.info("Successfully delivered event {} to {}. Status: {}", event.id(), sub.url(), response.getStatusCode()))
                .doOnError(error -> logger.error("Failed to deliver event {} to {}: {}", event.id(), sub.url(), error.getMessage()))
                .subscribe();
    }
    
    public List<WebhookSubscription> getSubscriptions(String eventType) {
        return subscriptions.getOrDefault(eventType, List.of());
    }
}
