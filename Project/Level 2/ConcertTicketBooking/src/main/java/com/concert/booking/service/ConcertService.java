package com.concert.booking.service;

import com.concert.booking.dto.response.ConcertDetailResponse;
import com.concert.booking.dto.response.ConcertSummaryResponse;
import com.concert.booking.dto.response.SeatResponse;

import java.util.List;

public interface ConcertService {
    List<ConcertSummaryResponse> getAllActiveConcerts();
    ConcertDetailResponse getConcertDetail(String concertId);
    List<SeatResponse> getConcertSeats(String concertId, String currentUserId);
}