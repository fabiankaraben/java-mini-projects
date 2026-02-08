package com.example.csvparser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Please upload a CSV file!"));
        }

        if (!"text/csv".equals(file.getContentType()) && !file.getOriginalFilename().endsWith(".csv")) {
             return ResponseEntity.badRequest().body(Map.of("message", "Invalid file type. Please upload a CSV file."));
        }

        try {
            List<Map<String, String>> data = csvService.parseCsv(file);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Could not parse CSV file: " + e.getMessage()));
        }
    }
}
