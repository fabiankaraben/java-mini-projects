package com.example.cache;

public class ExpensiveComputationService implements ComputationService {

    @Override
    public String compute(String input) {
        try {
            // Simulate expensive computation
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Computation interrupted", e);
        }
        return "Processed: " + input;
    }
}
