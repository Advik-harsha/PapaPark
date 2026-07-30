package com.parking.system.controller;

import com.parking.system.entity.User;
import com.parking.system.repository.UserRepository;
import com.parking.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // In-memory store for OTPs: email -> otp
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOpt.isEmpty()) {
            response.put("message", "User not found with this email!");
            return ResponseEntity.badRequest().body(response);
        }

        // Generate 6-digit OTP
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpStore.put(email, otp);

        emailService.sendOtpEmail(email, otp);

        response.put("message", "OTP sent successfully to " + email + ". (Test Mode OTP: " + otp + ")");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String email,
                                                              @RequestParam String otp,
                                                              @RequestParam String newPassword) {
        Map<String, String> response = new HashMap<>();
        String storedOtp = otpStore.get(email);

        if (storedOtp == null || !storedOtp.equals(otp)) {
            response.put("message", "Invalid or expired OTP code!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpStore.remove(email);

        response.put("message", "Password reset successfully! You can now login with your new password.");
        return ResponseEntity.ok(response);
    }
}
