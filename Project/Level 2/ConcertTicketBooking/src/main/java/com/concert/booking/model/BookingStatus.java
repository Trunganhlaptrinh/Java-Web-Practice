package com.concert.booking.model;

public enum BookingStatus {
    PENDING_PAYMENT, // Chá» thanh toÃ¡n (khÃ³a gháº¿ táº¡m thá»i 10 phÃºt)
    CONFIRMED,       // ÄÃ£ thanh toÃ¡n thÃ nh cÃ´ng, Ä‘Ã£ xuáº¥t vÃ©
    EXPIRED,         // QuÃ¡ háº¡n 10 phÃºt khÃ´ng thanh toÃ¡n -> tá»± Ä‘á»™ng nháº£ vÃ©
    CANCELLED        // KhÃ¡ch chá»§ Ä‘á»™ng há»§y Ä‘Æ¡n
}
