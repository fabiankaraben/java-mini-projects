package com.example.csvexport.controller;

import com.example.csvexport.service.CsvExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class CsvExportController {

    private final CsvExportService csvExportService;

    public CsvExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @GetMapping("/users")
    public void exportUsersToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=\"users.csv\"");
        csvExportService.writeUsersToCsv(response.getWriter());
    }
}
