package com.example.webhook.controller;

import com.example.webhook.model.WebhookSubscription;
import com.example.webhook.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/register")
    public ResponseEntity<WebhookSubscription> registerWebhook(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        String eventType = request.get("eventType");
        
        if (url == null || eventType == null) {
            return ResponseEntity.badRequest().build();
        }

        WebhookSubscription subscription = webhookService.registerSubscription(url, eventType);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/trigger")
    public ResponseEntity<Void> triggerEvent(@RequestBody Map<String, Object> request) {
        String eventType = (String) request.get("eventType");
        Object payload = request.get("payload");

        if (eventType == null || payload == null) {
            return ResponseEntity.badRequest().build();
        }

        webhookService.triggerEvent(eventType, payload);
        return ResponseEntity.accepted().build();
    }
}
