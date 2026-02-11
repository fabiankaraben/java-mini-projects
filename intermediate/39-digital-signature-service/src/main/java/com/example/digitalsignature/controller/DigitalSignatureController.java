package com.example.digitalsignature.controller;

import com.example.digitalsignature.model.*;
import com.example.digitalsignature.service.DigitalSignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@RestController
@RequestMapping("/api/signature")
public class DigitalSignatureController {

    private final DigitalSignatureService digitalSignatureService;

    public DigitalSignatureController(DigitalSignatureService digitalSignatureService) {
        this.digitalSignatureService = digitalSignatureService;
    }

    @PostMapping("/generate-keys")
    public ResponseEntity<KeyPairResponse> generateKeys() {
        try {
            KeyPair keyPair = digitalSignatureService.generateKeyPair();
            String publicKey = digitalSignatureService.encodeKey(keyPair.getPublic());
            String privateKey = digitalSignatureService.encodeKey(keyPair.getPrivate());
            return ResponseEntity.ok(new KeyPairResponse(publicKey, privateKey));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<SignResponse> sign(@RequestBody SignRequest request) {
        try {
            String signature = digitalSignatureService.sign(request.getData(), request.getPrivateKey());
            return ResponseEntity.ok(new SignResponse(signature, digitalSignatureService.getSignatureAlgorithm()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verify(@RequestBody VerifyRequest request) {
        try {
            boolean isValid = digitalSignatureService.verify(request.getData(), request.getSignature(), request.getPublicKey());
            String message = isValid ? "Signature is valid." : "Signature is invalid.";
            return ResponseEntity.ok(new VerifyResponse(isValid, message));
        } catch (Exception e) {
            return ResponseEntity.ok(new VerifyResponse(false, "Error verifying signature: " + e.getMessage()));
        }
    }
}
