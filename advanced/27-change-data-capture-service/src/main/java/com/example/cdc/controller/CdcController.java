package com.example.cdc.controller;

import com.example.cdc.model.CdcEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/events")
public class CdcController {

    private final List<CdcEvent> events = new CopyOnWriteArrayList<>();

    @EventListener
    public void handleCdcEvent(CdcEvent event) {
        // Keep only the last 100 events
        if (events.size() >= 100) {
            events.remove(0);
        }
        events.add(event);
    }

    @GetMapping
    public List<CdcEvent> getEvents() {
        return events;
    }
}
