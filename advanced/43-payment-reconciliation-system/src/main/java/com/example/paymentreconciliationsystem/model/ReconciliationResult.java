package com.example.paymentreconciliationsystem.model;

import java.util.ArrayList;
import java.util.List;

public class ReconciliationResult {
    private List<Transaction> matchedTransactions = new ArrayList<>();
    private List<Transaction> missingInBank = new ArrayList<>();
    private List<Transaction> missingInInternal = new ArrayList<>();
    private List<Discrepancy> discrepancies = new ArrayList<>();

    public void addMatched(Transaction t) {
        matchedTransactions.add(t);
    }

    public void addMissingInBank(Transaction t) {
        missingInBank.add(t);
    }

    public void addMissingInInternal(Transaction t) {
        missingInInternal.add(t);
    }

    public void addDiscrepancy(Discrepancy d) {
        discrepancies.add(d);
    }

    public List<Transaction> getMatchedTransactions() {
        return matchedTransactions;
    }

    public List<Transaction> getMissingInBank() {
        return missingInBank;
    }

    public List<Transaction> getMissingInInternal() {
        return missingInInternal;
    }

    public List<Discrepancy> getDiscrepancies() {
        return discrepancies;
    }

    public static class Discrepancy {
        private Transaction internal;
        private Transaction bank;
        private String reason;

        public Discrepancy(Transaction internal, Transaction bank, String reason) {
            this.internal = internal;
            this.bank = bank;
            this.reason = reason;
        }

        public Transaction getInternal() {
            return internal;
        }

        public Transaction getBank() {
            return bank;
        }

        public String getReason() {
            return reason;
        }
    }
}
