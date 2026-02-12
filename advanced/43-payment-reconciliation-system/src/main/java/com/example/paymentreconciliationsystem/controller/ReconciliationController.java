package com.example.paymentreconciliationsystem.controller;

import com.example.paymentreconciliationsystem.model.ReconciliationResult;
import com.example.paymentreconciliationsystem.service.ReconciliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ReconciliationResult> reconcileFiles(
            @RequestParam("internal") MultipartFile internalFile,
            @RequestParam("bank") MultipartFile bankFile) {
        try {
            ReconciliationResult result = reconciliationService.reconcile(internalFile, bankFile);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
