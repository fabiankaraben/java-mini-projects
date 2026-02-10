package com.example.notifications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return notificationService.subscribe();
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody String message) {
        notificationService.sendNotification(message);
        return ResponseEntity.ok().build();
    }
}
