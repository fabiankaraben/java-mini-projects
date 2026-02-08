package com.example.csvparser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    public List<Map<String, String>> parseCsv(MultipartFile file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            List<Map<String, String>> records = new ArrayList<>();
            List<String> headers = csvParser.getHeaderNames();

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> recordMap = new HashMap<>();
                for (String header : headers) {
                    if (csvRecord.isMapped(header)) {
                        recordMap.put(header, csvRecord.get(header));
                    }
                }
                records.add(recordMap);
            }

            return records;
        }
    }
}
