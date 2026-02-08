package com.example.timezoneconverter;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.TreeSet;

@Service
public class TimeZoneService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String convertTime(String time, String sourceZone, String targetZone) {
        try {
            // Parse the input time
            LocalDateTime localDateTime = LocalDateTime.parse(time, FORMATTER);

            // Create ZonedDateTime for the source zone
            ZoneId sourceZoneId = ZoneId.of(sourceZone);
            ZonedDateTime sourceZonedDateTime = ZonedDateTime.of(localDateTime, sourceZoneId);

            // Convert to target zone
            ZoneId targetZoneId = ZoneId.of(targetZone);
            ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZoneId);

            // Format the output
            return targetZonedDateTime.format(FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd HH:mm:ss'");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid zone ID or conversion error: " + e.getMessage());
        }
    }

    public Set<String> getAvailableZoneIds() {
        return new TreeSet<>(ZoneId.getAvailableZoneIds());
    }
}
