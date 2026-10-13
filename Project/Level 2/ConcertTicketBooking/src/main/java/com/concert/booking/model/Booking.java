package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id; // MÃ£ Ä‘Æ¡n hÃ ng (vÃ­ dá»¥: BKG-20261013-10293)

    @Indexed
    private String userId;

    private String userEmail;

    private String userFullName;

    private String concertId;

    private String concertTitle;

    @Builder.Default
    private List<BookingItem> items = new ArrayList<>();

    private BigDecimal totalAmount;

    private String voucherCode;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal finalAmount;

    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING_PAYMENT;

    private Instant holdExpiresAt; // Háº¿t háº¡n thanh toÃ¡n sau 10 phÃºt

    private String paymentMethod; // MOCK_MOMO | MOCK_VNPAY | BANK_TRANSFER

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
