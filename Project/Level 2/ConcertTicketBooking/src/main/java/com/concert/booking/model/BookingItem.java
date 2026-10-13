package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingItem {
    private String seatId;      // ID cá»§a gháº¿ trong collection seats
    private String seatNumber;  // VÃ­ dá»¥: A-01
    private String tierName;    // VVIP Lounge
    private BigDecimal price;   // GiÃ¡ cá»§a vÃ©
}
