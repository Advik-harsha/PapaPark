package com.parking.system.service;

import com.parking.system.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto processPayment(String sessionId, String paymentType);
    PaymentDto getPaymentDetails(String id);
    List<PaymentDto> getUserPayments(String userId);
    List<PaymentDto> getPendingPayments();
}
