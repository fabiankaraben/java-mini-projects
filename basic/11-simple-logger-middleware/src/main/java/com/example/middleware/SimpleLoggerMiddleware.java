package com.example.middleware;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SimpleLoggerMiddleware extends Filter {
    private static final Logger logger = Logger.getLogger(SimpleLoggerMiddleware.class.getName());

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        long startTime = System.currentTimeMillis();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            chain.doFilter(exchange);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            // Logging request method, path, and duration
            String logMessage = String.format("%s %s took %dms", method, path, duration);
            logger.info(logMessage);
        }
    }

    @Override
    public String description() {
        return "Simple Logger Middleware";
    }
}
