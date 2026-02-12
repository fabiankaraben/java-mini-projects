package com.example.paymentreconciliationsystem.service;

import com.example.paymentreconciliationsystem.model.ReconciliationResult;
import com.example.paymentreconciliationsystem.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReconciliationService {

    public ReconciliationResult reconcile(MultipartFile internalFile, MultipartFile bankFile) throws IOException {
        Map<String, Transaction> internalTransactions = parseCsv(internalFile, "INTERNAL");
        Map<String, Transaction> bankTransactions = parseCsv(bankFile, "BANK");

        ReconciliationResult result = new ReconciliationResult();

        // Check internal against bank
        for (Transaction internal : internalTransactions.values()) {
            Transaction bank = bankTransactions.get(internal.getTransactionId());
            if (bank == null) {
                result.addMissingInBank(internal);
            } else {
                if (internal.getAmount().compareTo(bank.getAmount()) != 0) {
                    result.addDiscrepancy(new ReconciliationResult.Discrepancy(
                            internal, bank, "Amount mismatch: Internal=" + internal.getAmount() + ", Bank=" + bank.getAmount()
                    ));
                } else {
                    result.addMatched(internal);
                }
            }
        }

        // Check for transactions in bank but not in internal
        for (Transaction bank : bankTransactions.values()) {
            if (!internalTransactions.containsKey(bank.getTransactionId())) {
                result.addMissingInInternal(bank);
            }
        }

        return result;
    }

    private Map<String, Transaction> parseCsv(MultipartFile file, String source) throws IOException {
        Map<String, Transaction> transactions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                String id = csvRecord.get("TransactionID");
                LocalDate date = LocalDate.parse(csvRecord.get("Date"));
                BigDecimal amount = new BigDecimal(csvRecord.get("Amount"));
                String description = csvRecord.get("Description");

                Transaction transaction = new Transaction(id, date, amount, description, source);
                transactions.put(id, transaction);
            }
        }
        return transactions;
    }
}
