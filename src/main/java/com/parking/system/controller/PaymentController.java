package com.parking.system.controller;

import com.parking.system.dto.PaymentDto;
import com.parking.system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentDto> processPayment(@RequestParam String sessionId,
                                                      @RequestParam String paymentType) {
        PaymentDto payment = paymentService.processPayment(sessionId, paymentType);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentDto> getPaymentDetails(@PathVariable String id) {
        PaymentDto payment = paymentService.getPaymentDetails(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PaymentDto>> getUserPayments(@PathVariable String userId) {
        List<PaymentDto> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDto>> getPendingPayments() {
        List<PaymentDto> payments = paymentService.getPendingPayments();
        return ResponseEntity.ok(payments);
    }
}
