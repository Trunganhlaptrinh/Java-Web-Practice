package com.concert.booking.scheduler;

import com.concert.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;

    /**
     * Cháº¡y Ä‘á»‹nh ká»³ má»—i 30 giÃ¢y Ä‘á»ƒ tá»± Ä‘á»™ng quÃ©t vÃ  giáº£i phÃ³ng cÃ¡c gháº¿ bá»‹ giá»¯ quÃ¡ háº¡n 10 phÃºt
     */
    @Scheduled(fixedRate = 30000)
    public void scanAndReleaseExpiredHolds() {
        try {
            bookingService.releaseExpiredBookings();
        } catch (Exception ex) {
            log.error("Lá»—i trong quÃ¡ trÃ¬nh quÃ©t giáº£i phÃ³ng Ä‘Æ¡n giá»¯ chá»— quÃ¡ háº¡n: {}", ex.getMessage());
        }
    }
}