package com.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log")
    public String generateLog(@RequestParam(defaultValue = "INFO") String level, @RequestParam(defaultValue = "Hello from Spring Boot!") String message) {
        switch (level.toUpperCase()) {
            case "DEBUG":
                logger.debug("DEBUG Log: {}", message);
                break;
            case "WARN":
                logger.warn("WARN Log: {}", message);
                break;
            case "ERROR":
                logger.error("ERROR Log: {}", message);
                break;
            case "TRACE":
                logger.trace("TRACE Log: {}", message);
                break;
            default:
                logger.info("INFO Log: {}", message);
        }
        return "Logged message: " + message + " at level: " + level;
    }

    @GetMapping("/exception")
    public String generateException() {
        try {
            throw new RuntimeException("Simulated exception for logging");
        } catch (Exception e) {
            logger.error("Caught exception: ", e);
            return "Exception logged";
        }
    }
}
