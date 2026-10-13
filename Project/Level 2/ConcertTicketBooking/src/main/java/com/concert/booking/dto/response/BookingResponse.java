package com.concert.booking.dto.response;

import com.concert.booking.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private String bookingCode;
    private String concertId;
    private String concertTitle;
    private String venue;
    private Instant concertStartTime;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private BookingStatus status;
    private Instant holdExpiresAt;
    private long remainingHoldSeconds;
    private List<BookingItemDto> items;
    private String voucherCode;
    private String paymentMethod;
    private Instant createdAt;
}