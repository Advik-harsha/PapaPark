package com.parking.system.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendOtpEmail(String to, String otp);
    void sendBookingConfirmation(String to, String vehicleNumber, String slotNumber);
    void sendPaymentReceipt(String to, String transactionId, String amount);
}
