package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "seats")
@CompoundIndexes({
    @CompoundIndex(name = "concert_seat_unique", def = "{'concertId': 1, 'seatNumber': 1}", unique = true),
    @CompoundIndex(name = "status_hold_expiry", def = "{'status': 1, 'holdExpiresAt': 1}")
})
public class Seat {

    @Id
    private String id;

    @Indexed
    private String concertId;

    private String tierId; // VVIP, VIP, CAT1...

    private String seatNumber; // VÃ­ dá»¥: A-01, B-12

    private String row; // HÃ ng: A, B, C...

    private int col; // Cá»™t: 1, 2, 3...

    @Builder.Default
    private SeatStatus status = SeatStatus.AVAILABLE;

    private String heldByUserId; // User ID Ä‘ang giá»¯ gháº¿

    private Instant holdExpiresAt; // Thá»i Ä‘iá»ƒm háº¿t háº¡n giá»¯ gháº¿
}
