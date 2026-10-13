package com.concert.booking.dto.response;

import com.concert.booking.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private String id;
    private String ticketCode;
    private String bookingCode;
    private String concertTitle;
    private String venue;
    private Instant concertStartTime;
    private String seatCode;
    private String tierName;
    private BigDecimal price;
    private String qrCodeData;
    private TicketStatus status;
    private Instant issuedAt;
}