package com.example.timezoneconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TimeZoneController {

    private final TimeZoneService timeZoneService;

    @Autowired
    public TimeZoneController(TimeZoneService timeZoneService) {
        this.timeZoneService = timeZoneService;
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convertTime(
            @RequestParam String time,
            @RequestParam String sourceZone,
            @RequestParam String targetZone) {
        try {
            String convertedTime = timeZoneService.convertTime(time, sourceZone, targetZone);
            return ResponseEntity.ok(Map.of(
                    "originalTime", time,
                    "sourceZone", sourceZone,
                    "targetZone", targetZone,
                    "convertedTime", convertedTime
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/zones")
    public ResponseEntity<Set<String>> getAvailableZones() {
        return ResponseEntity.ok(timeZoneService.getAvailableZoneIds());
    }
}
