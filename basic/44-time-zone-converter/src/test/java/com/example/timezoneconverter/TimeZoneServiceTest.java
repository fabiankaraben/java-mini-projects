package com.example.timezoneconverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TimeZoneServiceTest {

    @Autowired
    private TimeZoneService timeZoneService;

    @Test
    public void testConvertTime_StandardCase() {
        // UTC to America/New_York (Winter/Standard Time)
        // 2024-01-01 12:00:00 UTC -> 2024-01-01 07:00:00 EST
        String result = timeZoneService.convertTime("2024-01-01 12:00:00", "UTC", "America/New_York");
        assertEquals("2024-01-01 07:00:00", result);
    }

    @Test
    public void testConvertTime_DST_Summer() {
        // UTC to America/New_York (Summer/DST)
        // 2024-06-01 12:00:00 UTC -> 2024-06-01 08:00:00 EDT
        String result = timeZoneService.convertTime("2024-06-01 12:00:00", "UTC", "America/New_York");
        assertEquals("2024-06-01 08:00:00", result);
    }

    @Test
    public void testConvertTime_DST_SpringForward_Gap() {
        // In 2024, DST started in NY on March 10 at 2:00 AM local time.
        // Clocks jumped from 01:59:59 EST to 03:00:00 EDT.
        // 07:00 UTC corresponds to the moment the jump happens (2am EST -> 3am EDT).
        
        // 2024-03-10 06:59:00 UTC -> 2024-03-10 01:59:00 EST
        String beforeJump = timeZoneService.convertTime("2024-03-10 06:59:00", "UTC", "America/New_York");
        assertEquals("2024-03-10 01:59:00", beforeJump);

        // 2024-03-10 07:00:00 UTC -> 2024-03-10 03:00:00 EDT
        String atJump = timeZoneService.convertTime("2024-03-10 07:00:00", "UTC", "America/New_York");
        assertEquals("2024-03-10 03:00:00", atJump);
    }

    @Test
    public void testConvertTime_DST_FallBack_Overlap() {
        // In 2024, DST ended in NY on Nov 3 at 2:00 AM local time.
        // Clocks jumped back from 02:00:00 EDT to 01:00:00 EST.
        // So 01:00:00 to 01:59:59 happens twice.
        
        // 05:30 UTC is still EDT (01:30 EDT)
        String firstOccurrence = timeZoneService.convertTime("2024-11-03 05:30:00", "UTC", "America/New_York");
        assertEquals("2024-11-03 01:30:00", firstOccurrence);
        
        // 06:30 UTC is now EST (01:30 EST)
        String secondOccurrence = timeZoneService.convertTime("2024-11-03 06:30:00", "UTC", "America/New_York");
        assertEquals("2024-11-03 01:30:00", secondOccurrence);
    }

    @Test
    public void testConvertTime_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            timeZoneService.convertTime("2024/01/01", "UTC", "America/New_York");
        });
    }

    @Test
    public void testConvertTime_InvalidZone() {
        assertThrows(IllegalArgumentException.class, () -> {
            timeZoneService.convertTime("2024-01-01 12:00:00", "Mars/Crater", "America/New_York");
        });
    }
}
