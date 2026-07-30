package com.parking.system.service.impl;

import com.parking.system.service.SmsService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    @Async
    public void sendSms(String phoneNumber, String message) {
        // Simulated Twilio / Fast2SMS API Dispatch
        System.out.println("==================================================");
        System.out.println("📱 [SIMULATED SMS SENT]");
        System.out.println("To Phone : " + (phoneNumber != null ? phoneNumber : "+91-9876543210"));
        System.out.println("Message  : " + message);
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        System.out.println("==================================================");
    }

    @Override
    public void sendBookingSms(String phoneNumber, String vehicleNumber, String slotNumber) {
        String msg = "SmartPark: Your parking session for " + vehicleNumber + " in slot " + slotNumber 
                + " has STARTED. Live tracking available in dashboard.";
        sendSms(phoneNumber, msg);
    }

    @Override
    public void sendPaymentSms(String phoneNumber, String transactionId, String amount) {
        String msg = "SmartPark: Payment of Rs." + amount + " received for TxID " + transactionId 
                + ". Thank you for using SmartPark!";
        sendSms(phoneNumber, msg);
    }

    @Override
    public void sendExpiryReminderSms(String phoneNumber, String slotNumber, int minutesRemaining) {
        String msg = "SmartPark ALERT: Your parking session in slot " + slotNumber 
                + " will cross the hourly limit in " + minutesRemaining + " minutes. Additional hourly rate applies.";
        sendSms(phoneNumber, msg);
    }
}
