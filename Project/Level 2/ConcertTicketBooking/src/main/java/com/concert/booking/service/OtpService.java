package com.concert.booking.service;

public interface OtpService {
    String generateAndSendOtp(String targetEmail);
    boolean verifyOtp(String targetEmail, String otpCode);
}
