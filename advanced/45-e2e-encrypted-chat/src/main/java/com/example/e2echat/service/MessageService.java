package com.example.e2echat.service;

import com.example.e2echat.model.EncryptedMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {
    // Recipient -> List of Messages
    private final ConcurrentHashMap<String, List<EncryptedMessage>> messageStore = new ConcurrentHashMap<>();

    public void sendMessage(EncryptedMessage message) {
        messageStore.computeIfAbsent(message.getRecipient(), k -> new ArrayList<>()).add(message);
    }

    public List<EncryptedMessage> getMessages(String recipient) {
        return messageStore.getOrDefault(recipient, new ArrayList<>());
    }
}
