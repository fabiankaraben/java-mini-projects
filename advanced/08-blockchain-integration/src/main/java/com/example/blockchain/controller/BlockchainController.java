package com.example.blockchain.controller;

import com.example.blockchain.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getClientVersion() {
        try {
            String version = blockchainService.getClientVersion();
            return ResponseEntity.ok(Map.of("version", version));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/block-number")
    public ResponseEntity<Map<String, Object>> getBlockNumber() {
        try {
            BigInteger blockNumber = blockchainService.getBlockNumber();
            return ResponseEntity.ok(Map.of("blockNumber", blockNumber));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/balance/{address}")
    public ResponseEntity<Map<String, String>> getBalance(@PathVariable String address) {
        try {
            String balance = blockchainService.getBalance(address);
            return ResponseEntity.ok(Map.of("address", address, "balance_eth", balance));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
