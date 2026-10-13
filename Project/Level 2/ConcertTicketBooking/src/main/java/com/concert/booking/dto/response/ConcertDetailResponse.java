package com.concert.booking.dto.response;

import com.concert.booking.model.TicketTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertDetailResponse {
    private String id;
    private String title;
    private String artist;
    private String venue;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private String posterUrl;
    private String bannerUrl;
    private String status;
    private List<TicketTier> tiers;
    private int totalSeats;
    private int availableSeats;
}