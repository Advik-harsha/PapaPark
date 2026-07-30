package com.parking.system.service.impl;

import com.parking.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            if (mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("noreply@smartpark.com");
                mailSender.send(message);
            } else {
                System.out.println("[SIMULATED EMAIL] To: " + to + " | Subject: " + subject + "\n" + body);
            }
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            System.out.println("[SIMULATED EMAIL FALLBACK] To: " + to + " | Subject: " + subject + "\n" + body);
        }
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        String subject = "🔑 Your SmartPark Password Reset OTP";
        String body = "Hello,\n\nYour 6-digit OTP code to reset your SmartPark account password is:\n\n"
                + "👉 " + otp + "\n\nThis OTP is valid for 10 minutes. Do not share this OTP with anyone.\n\nSmartPark Security Team";
        sendEmail(to, subject, body);
    }

    @Override
    public void sendBookingConfirmation(String to, String vehicleNumber, String slotNumber) {
        String subject = "🅿️ SmartPark Parking Booking Confirmed";
        String body = "Hello,\n\nYour parking session has been successfully started!\n\n"
                + "Vehicle: " + vehicleNumber + "\nSlot: " + slotNumber + "\nTime: " + java.time.LocalDateTime.now() + "\n\n"
                + "Thank you for using SmartPark!";
        sendEmail(to, subject, body);
    }

    @Override
    public void sendPaymentReceipt(String to, String transactionId, String amount) {
        String subject = "💳 SmartPark Payment Receipt";
        String body = "Hello,\n\nYour payment of ₹" + amount + " for transaction ID " + transactionId + " was successful!\n\nSmartPark Team";
        sendEmail(to, subject, body);
    }
}
