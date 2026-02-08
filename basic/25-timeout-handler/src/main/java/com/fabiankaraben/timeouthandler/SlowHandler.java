package com.fabiankaraben.timeouthandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class SlowHandler implements HttpHandler {
    private final long sleepDurationMillis;

    public SlowHandler(long sleepDurationMillis) {
        this.sleepDurationMillis = sleepDurationMillis;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Thread.sleep(sleepDurationMillis);
            String response = "Process complete";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (InterruptedException e) {
            // Thread was interrupted, likely by timeout handler
            // Do not write to exchange as it might be closed or handled by caller
            Thread.currentThread().interrupt();
        }
    }
}
