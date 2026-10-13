package com.concert.booking.service.impl;

import com.concert.booking.dto.response.ConcertDetailResponse;
import com.concert.booking.dto.response.ConcertSummaryResponse;
import com.concert.booking.dto.response.SeatResponse;
import com.concert.booking.exception.ResourceNotFoundException;
import com.concert.booking.model.Concert;
import com.concert.booking.model.Seat;
import com.concert.booking.model.SeatStatus;
import com.concert.booking.model.TicketTier;
import com.concert.booking.repository.ConcertRepository;
import com.concert.booking.repository.SeatRepository;
import com.concert.booking.service.ConcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    @Override
    public List<ConcertSummaryResponse> getAllActiveConcerts() {
        List<Concert> concerts = concertRepository.findAll();

        return concerts.stream()
                .filter(c -> !"CANCELLED".equalsIgnoreCase(c.getStatus()))
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConcertDetailResponse getConcertDetail(String concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ResourceNotFoundException("Concert", "id", concertId));

        List<Seat> seats = seatRepository.findByConcertIdOrderByRowAscColAsc(concertId);
        int totalSeats = seats.size();

        Instant now = Instant.now();
        int availableSeats = (int) seats.stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE || 
                            (s.getStatus() == SeatStatus.HELD && s.getHoldExpiresAt() != null && s.getHoldExpiresAt().isBefore(now)))
                .count();

        return ConcertDetailResponse.builder()
                .id(concert.getId())
                .title(concert.getTitle())
                .artist(concert.getArtist())
                .venue(concert.getVenue())
                .description(concert.getDescription())
                .startTime(concert.getEventDate())
                .posterUrl(concert.getBannerUrl())
                .bannerUrl(concert.getBannerUrl())
                .status(concert.getStatus())
                .tiers(concert.getTiers() != null ? concert.getTiers() : Collections.emptyList())
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .build();
    }

    @Override
    public List<SeatResponse> getConcertSeats(String concertId, String currentUserId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ResourceNotFoundException("Concert", "id", concertId));

        Map<String, TicketTier> tierMap = Optional.ofNullable(concert.getTiers())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(TicketTier::getTierId, Function.identity(), (t1, t2) -> t1));

        List<Seat> seats = seatRepository.findByConcertIdOrderByRowAscColAsc(concertId);
        Instant now = Instant.now();

        return seats.stream().map(seat -> {
            TicketTier tier = tierMap.get(seat.getTierId());

            SeatStatus effectiveStatus = seat.getStatus();
            // Náº¿u gháº¿ Ä‘ang bá»‹ giá»¯ (HELD) nhÆ°ng Ä‘Ã£ quÃ¡ háº¡n thÃ¬ coi nhÆ° AVAILABLE
            if (effectiveStatus == SeatStatus.HELD && seat.getHoldExpiresAt() != null && seat.getHoldExpiresAt().isBefore(now)) {
                effectiveStatus = SeatStatus.AVAILABLE;
            }

            boolean isHeldByCurrentUser = currentUserId != null 
                    && currentUserId.equals(seat.getHeldByUserId()) 
                    && seat.getStatus() == SeatStatus.HELD
                    && (seat.getHoldExpiresAt() == null || seat.getHoldExpiresAt().isAfter(now));

            return SeatResponse.builder()
                    .id(seat.getId())
                    .concertId(seat.getConcertId())
                    .tierId(seat.getTierId())
                    .tierName(tier != null ? tier.getName() : seat.getTierId())
                    .tierColor(tier != null ? tier.getColor() : "#1E90FF")
                    .row(seat.getRow())
                    .seatNumber(seat.getCol())
                    .seatCode(seat.getSeatNumber())
                    .price(tier != null ? tier.getPrice() : BigDecimal.ZERO)
                    .status(effectiveStatus)
                    .isHeldByCurrentUser(isHeldByCurrentUser)
                    .build();
        }).collect(Collectors.toList());
    }

    private ConcertSummaryResponse mapToSummaryResponse(Concert concert) {
        BigDecimal minPrice = BigDecimal.ZERO;
        BigDecimal maxPrice = BigDecimal.ZERO;

        if (concert.getTiers() != null && !concert.getTiers().isEmpty()) {
            minPrice = concert.getTiers().stream()
                    .map(TicketTier::getPrice)
                    .filter(Objects::nonNull)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            maxPrice = concert.getTiers().stream()
                    .map(TicketTier::getPrice)
                    .filter(Objects::nonNull)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        }

        List<Seat> seats = seatRepository.findByConcertIdOrderByRowAscColAsc(concert.getId());
        int totalSeats = seats.size();
        Instant now = Instant.now();
        int availableSeats = (int) seats.stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE || 
                            (s.getStatus() == SeatStatus.HELD && s.getHoldExpiresAt() != null && s.getHoldExpiresAt().isBefore(now)))
                .count();

        return ConcertSummaryResponse.builder()
                .id(concert.getId())
                .title(concert.getTitle())
                .artist(concert.getArtist())
                .venue(concert.getVenue())
                .startTime(concert.getEventDate())
                .posterUrl(concert.getBannerUrl())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .status(concert.getStatus())
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .build();
    }
}