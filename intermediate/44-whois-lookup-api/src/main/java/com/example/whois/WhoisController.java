package com.example.whois;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/whois")
public class WhoisController {

    private final WhoisService whoisService;

    public WhoisController(WhoisService whoisService) {
        this.whoisService = whoisService;
    }

    @GetMapping
    public ResponseEntity<?> lookup(
            @RequestParam String domain, 
            @RequestParam(required = false) String server,
            @RequestParam(required = false) Integer port) {
        try {
            String result;
            if (server != null && !server.isBlank()) {
                if (port != null) {
                    result = whoisService.getWhoisInfo(domain, server, port);
                } else {
                    result = whoisService.getWhoisInfo(domain, server);
                }
            } else {
                result = whoisService.getWhoisInfo(domain);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("domain", domain);
            response.put("whois_server", server != null ? server : "default");
            if (port != null) {
                response.put("port", port);
            }
            response.put("result", result);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to query WHOIS server: " + e.getMessage()));
        }
    }
}
