package com.example.paymentreconciliationsystem.service;

import com.example.paymentreconciliationsystem.model.ReconciliationResult;
import com.example.paymentreconciliationsystem.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReconciliationServiceTest {

    private final ReconciliationService service = new ReconciliationService();

    @Test
    void testReconciliationWithMismatches() throws IOException {
        // Internal Data:
        // T1: Match
        // T2: Amount Mismatch
        // T3: Missing in Bank
        String internalCsvContent = "TransactionID,Date,Amount,Description\n" +
                "T1,2023-10-01,100.00,Payment 1\n" +
                "T2,2023-10-02,200.00,Payment 2\n" +
                "T3,2023-10-03,300.00,Payment 3";

        // Bank Data:
        // T1: Match
        // T2: Amount Mismatch (205.00)
        // T4: Missing in Internal
        String bankCsvContent = "TransactionID,Date,Amount,Description\n" +
                "T1,2023-10-01,100.00,Payment 1\n" +
                "T2,2023-10-02,205.00,Payment 2\n" +
                "T4,2023-10-04,400.00,Payment 4";

        MockMultipartFile internalFile = new MockMultipartFile("internal", "internal.csv", "text/csv", internalCsvContent.getBytes());
        MockMultipartFile bankFile = new MockMultipartFile("bank", "bank.csv", "text/csv", bankCsvContent.getBytes());

        ReconciliationResult result = service.reconcile(internalFile, bankFile);

        // Verify Matched
        List<Transaction> matched = result.getMatchedTransactions();
        assertEquals(1, matched.size());
        assertEquals("T1", matched.get(0).getTransactionId());

        // Verify Missing in Bank
        List<Transaction> missingInBank = result.getMissingInBank();
        assertEquals(1, missingInBank.size());
        assertEquals("T3", missingInBank.get(0).getTransactionId());

        // Verify Missing in Internal
        List<Transaction> missingInInternal = result.getMissingInInternal();
        assertEquals(1, missingInInternal.size());
        assertEquals("T4", missingInInternal.get(0).getTransactionId());

        // Verify Discrepancies
        List<ReconciliationResult.Discrepancy> discrepancies = result.getDiscrepancies();
        assertEquals(1, discrepancies.size());
        ReconciliationResult.Discrepancy discrepancy = discrepancies.get(0);
        assertEquals("T2", discrepancy.getInternal().getTransactionId());
        assertEquals(new BigDecimal("200.00"), discrepancy.getInternal().getAmount());
        assertEquals(new BigDecimal("205.00"), discrepancy.getBank().getAmount());
        assertTrue(discrepancy.getReason().contains("Amount mismatch"));
    }
}
