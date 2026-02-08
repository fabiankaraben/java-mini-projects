package com.fabiankaraben.timeouthandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.*;

public class TimeoutHandler implements HttpHandler {
    private final HttpHandler delegate;
    private final Duration timeout;
    private final ExecutorService executor;

    public TimeoutHandler(HttpHandler delegate, Duration timeout) {
        this.delegate = delegate;
        this.timeout = timeout;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Future<?> future = executor.submit(() -> {
            try {
                delegate.handle(exchange);
            } catch (Exception e) {
                // Log exception if needed
                e.printStackTrace();
            }
        });

        try {
            future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            // Only send error if response hasn't been sent yet
            try {
                String response = "Request timed out";
                exchange.sendResponseHeaders(503, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (IOException ioException) {
                // Response might have been already committed
            }
        } catch (InterruptedException | ExecutionException e) {
            future.cancel(true);
            try {
                String response = "Internal Server Error";
                exchange.sendResponseHeaders(500, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (IOException ioException) {
                // Ignore
            }
        }
    }
}
