package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id; // MÃ£ vÃ© duy nháº¥t (vÃ­ dá»¥: TCK-89421)

    @Indexed
    private String bookingId;

    @Indexed
    private String userId;

    private String concertId;

    private String concertTitle;

    private String venue;

    private Instant eventDate;

    private String tierName;

    private String seatNumber;

    private BigDecimal price;

    @Indexed(unique = true)
    private String qrCodeData; // Chuá»—i token báº£o máº­t Ä‘á»ƒ render QR code

    @Builder.Default
    private TicketStatus status = TicketStatus.VALID;

    @CreatedDate
    private Instant createdAt;

    private Instant checkedInAt;
}
