package com.example.collaboration.controller;

import com.example.collaboration.model.Document;
import com.example.collaboration.model.Operation;
import com.example.collaboration.service.OtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollaborationController {

    private final OtService otService;

    @Autowired
    public CollaborationController(OtService otService) {
        this.otService = otService;
    }

    @MessageMapping("/edit")
    @SendTo("/topic/updates")
    public Operation handleEdit(Operation operation) {
        // Apply operation using OT
        return otService.applyOperation(operation);
    }

    @GetMapping("/api/document")
    public Document getDocument() {
        return otService.getDocument();
    }
}
