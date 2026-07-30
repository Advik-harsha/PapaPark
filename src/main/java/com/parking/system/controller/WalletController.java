package com.parking.system.controller;

import com.parking.system.dto.WalletTransactionDto;
import com.parking.system.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/add-money")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<WalletTransactionDto> addMoney(@RequestParam String userId,
                                                          @RequestParam BigDecimal amount) {
        WalletTransactionDto transaction = walletService.addMoney(userId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/deduct-money")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<WalletTransactionDto> deductMoney(@RequestParam String userId,
                                                             @RequestParam BigDecimal amount,
                                                             @RequestParam String remarks) {
        WalletTransactionDto transaction = walletService.deductMoney(userId, amount, remarks);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/balance/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable String userId) {
        BigDecimal balance = walletService.getWalletBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/history/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<WalletTransactionDto>> getWalletHistory(@PathVariable String userId) {
        List<WalletTransactionDto> history = walletService.getWalletHistory(userId);
        return ResponseEntity.ok(history);
    }
}
