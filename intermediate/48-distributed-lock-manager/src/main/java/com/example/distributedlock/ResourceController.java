package com.example.distributedlock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final DistributedLockService lockService;

    @PostMapping("/{id}/access")
    public ResponseEntity<String> accessResource(@PathVariable String id, @RequestParam(defaultValue = "10") long ttl) {
        String token = lockService.acquireLock(id, ttl);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Resource is locked");
        }
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<String> releaseResource(@PathVariable String id, @RequestParam String token) {
        boolean released = lockService.releaseLock(id, token);
        if (released) {
            return ResponseEntity.ok("Resource released");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or lock expired");
        }
    }
}
