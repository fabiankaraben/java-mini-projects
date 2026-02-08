package com.example.passwordhasher;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/hash")
    public Map<String, String> hash(@RequestBody PasswordRequest request) {
        String hash = passwordService.hashPassword(request.getPassword());
        return Collections.singletonMap("hash", hash);
    }

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody PasswordVerifyRequest request) {
        boolean match = passwordService.verifyPassword(request.getPassword(), request.getHash());
        return Collections.singletonMap("match", match);
    }
}
