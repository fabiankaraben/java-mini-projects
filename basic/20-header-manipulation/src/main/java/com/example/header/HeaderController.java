package com.example.header;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HeaderController {

    @GetMapping("/api/headers")
    public ResponseEntity<Map<String, String>> handleHeaders(
            @RequestHeader(value = "X-Custom-Input", defaultValue = "default") String customInput,
            @RequestHeader HttpHeaders headers) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("receivedCustomInput", customInput);
        responseBody.put("message", "Headers processed successfully");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Custom-Output", "Processed: " + customInput);
        responseHeaders.set("X-Server-Timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseBody);
    }
}
