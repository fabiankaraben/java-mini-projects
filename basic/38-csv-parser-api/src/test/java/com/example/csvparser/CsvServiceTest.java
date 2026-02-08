package com.example.csvparser;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvServiceTest {

    private final CsvService csvService = new CsvService();

    @Test
    void parseCsv_ShouldReturnListOfMaps_WhenCsvIsValid() throws IOException {
        String csvContent = "name,age,city\nJohn,30,New York\nJane,25,London";
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        List<Map<String, String>> result = csvService.parseCsv(file);

        assertNotNull(result);
        assertEquals(2, result.size());

        Map<String, String> firstRecord = result.get(0);
        assertEquals("John", firstRecord.get("name"));
        assertEquals("30", firstRecord.get("age"));
        assertEquals("New York", firstRecord.get("city"));

        Map<String, String> secondRecord = result.get(1);
        assertEquals("Jane", secondRecord.get("name"));
        assertEquals("25", secondRecord.get("age"));
        assertEquals("London", secondRecord.get("city"));
    }

    @Test
    void parseCsv_ShouldReturnEmptyList_WhenCsvIsEmpty() throws IOException {
        String csvContent = "name,age,city"; // Only headers
        MultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        List<Map<String, String>> result = csvService.parseCsv(file);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
