package com.example.twofactorauth.controller;

import com.example.twofactorauth.model.TotpSecretResponse;
import com.example.twofactorauth.model.TotpValidationRequest;
import com.example.twofactorauth.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/totp")
public class TotpController {

    private final TotpService totpService;

    public TotpController(TotpService totpService) {
        this.totpService = totpService;
    }

    @PostMapping("/generate")
    public ResponseEntity<TotpSecretResponse> generateSecret(@RequestParam(defaultValue = "user@example.com") String accountName) {
        GoogleAuthenticatorKey key = totpService.generateSecret();
        String qrCodeUrl = totpService.getQrCodeUrl(key, accountName);
        return ResponseEntity.ok(new TotpSecretResponse(key.getKey(), qrCodeUrl));
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCode(@RequestBody TotpValidationRequest request) {
        boolean isValid = totpService.validateCode(request.getSecret(), request.getCode());
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}
