package com.parking.system.service;

public interface SmsService {
    void sendSms(String phoneNumber, String message);
    void sendBookingSms(String phoneNumber, String vehicleNumber, String slotNumber);
    void sendPaymentSms(String phoneNumber, String transactionId, String amount);
    void sendExpiryReminderSms(String phoneNumber, String slotNumber, int minutesRemaining);
}
