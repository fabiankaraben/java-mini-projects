package com.example.e2echat.controller;

import com.example.e2echat.model.EncryptedMessage;
import com.example.e2echat.model.UserKey;
import com.example.e2echat.service.KeyService;
import com.example.e2echat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final KeyService keyService;
    private final MessageService messageService;

    public ChatController(KeyService keyService, MessageService messageService) {
        this.keyService = keyService;
        this.messageService = messageService;
    }

    @PostMapping("/keys/register")
    public ResponseEntity<Void> registerKey(@RequestBody UserKey userKey) {
        keyService.registerKey(userKey);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/keys/{username}")
    public ResponseEntity<String> getKey(@PathVariable String username) {
        String key = keyService.getKey(username);
        if (key != null) {
            return ResponseEntity.ok(key);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/chat/send")
    public ResponseEntity<Void> sendMessage(@RequestBody EncryptedMessage message) {
        message.setTimestamp(System.currentTimeMillis());
        messageService.sendMessage(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat/messages")
    public ResponseEntity<List<EncryptedMessage>> getMessages(@RequestParam String username) {
        return ResponseEntity.ok(messageService.getMessages(username));
    }
}
