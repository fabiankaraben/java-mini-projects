package com.example.csvexport.service;

import com.example.csvexport.model.User;
import com.example.csvexport.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class CsvExportService {

    private final UserRepository userRepository;

    public CsvExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void writeUsersToCsv(Writer writer) {
        List<User> users = userRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Username", "Email", "Role", "Active"))) {
            for (User user : users) {
                csvPrinter.printRecord(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.isActive());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while writing CSV ", e);
        }
    }
}
