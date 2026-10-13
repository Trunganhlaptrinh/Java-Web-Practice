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
public class TicketTier {
    private String tierId;       // VVIP, VIP, CAT1, CAT2
    private String name;         // TÃªn hiá»ƒn thá»‹: "VVIP Lounge", "KhÃ¡n Ä‘Ã i CAT 1"
    private BigDecimal price;    // GiÃ¡ vÃ©
    private String color;        // MÃ£ mÃ u hex sÆ¡ Ä‘á»“ gháº¿ (#FFD700, #FF4500, #1E90FF)
    private int totalCapacity;   // Tá»•ng sá»‘ lÆ°á»£ng gháº¿ cá»§a háº¡ng nÃ y
    private int availableCount;  // Sá»‘ gháº¿ cÃ²n trá»‘ng
}
