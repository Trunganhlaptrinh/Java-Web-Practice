package com.concert.booking.dto.response;

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
public class ConcertSummaryResponse {
    private String id;
    private String title;
    private String artist;
    private String venue;
    private Instant startTime;
    private String posterUrl;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String status;
    private int totalSeats;
    private int availableSeats;
}