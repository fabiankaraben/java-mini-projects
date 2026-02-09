package com.example.ratelimiter;

import java.util.concurrent.atomic.AtomicReference;

public class TokenBucket {
    private final long capacity;
    private final double refillRate; // tokens per second
    private final AtomicReference<State> state;

    private static class State {
        final double tokens;
        final long lastRefillTimestamp;

        State(double tokens, long lastRefillTimestamp) {
            this.tokens = tokens;
            this.lastRefillTimestamp = lastRefillTimestamp;
        }
    }

    public TokenBucket(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.state = new AtomicReference<>(new State(capacity, System.nanoTime()));
    }

    public boolean tryConsume() {
        while (true) {
            State currentState = state.get();
            long now = System.nanoTime();
            
            // Calculate refilled tokens
            long timeElapsed = now - currentState.lastRefillTimestamp;
            double tokensToAdd = (timeElapsed / 1_000_000_000.0) * refillRate;
            
            double newTokens = Math.min(capacity, currentState.tokens + tokensToAdd);
            
            if (newTokens < 1.0) {
                return false;
            }
            
            State nextState = new State(newTokens - 1.0, now);
            if (state.compareAndSet(currentState, nextState)) {
                return true;
            }
            // implementation note: if CAS fails, we retry loop
        }
    }
}
