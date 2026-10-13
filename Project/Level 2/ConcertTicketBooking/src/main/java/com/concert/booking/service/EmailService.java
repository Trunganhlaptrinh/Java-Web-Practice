package com.concert.booking.service;

import com.concert.booking.dto.request.EmailBotConfigRequest;
import com.concert.booking.dto.response.EmailBotConfigResponse;
import com.concert.booking.model.Booking;
import com.concert.booking.model.Ticket;

import java.util.List;

public interface EmailService {
    void sendBookingConfirmationEmail(Booking booking, List<Ticket> tickets);
    boolean sendOtpEmail(String targetEmail, String otpCode);
    EmailBotConfigResponse saveEmailBotConfig(String adminUsername, EmailBotConfigRequest request);
    EmailBotConfigResponse getEmailBotConfig();
}
