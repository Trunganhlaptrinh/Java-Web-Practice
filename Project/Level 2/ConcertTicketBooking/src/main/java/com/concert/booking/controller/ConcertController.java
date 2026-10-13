package com.concert.booking.controller;

import com.concert.booking.dto.response.ApiResponse;
import com.concert.booking.dto.response.ConcertDetailResponse;
import com.concert.booking.dto.response.ConcertSummaryResponse;
import com.concert.booking.dto.response.SeatResponse;
import com.concert.booking.model.User;
import com.concert.booking.service.AuthService;
import com.concert.booking.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConcertSummaryResponse>>> getAllActiveConcerts() {
        List<ConcertSummaryResponse> concerts = concertService.getAllActiveConcerts();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sự kiện thành công", concerts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConcertDetailResponse>> getConcertDetail(@PathVariable String id) {
        ConcertDetailResponse concert = concertService.getConcertDetail(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết sự kiện thành công", concert));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getConcertSeats(@PathVariable String id) {
        String currentUserId = null;
        try {
            User user = authService.getCurrentAuthenticatedUser();
            if (user != null) {
                currentUserId = user.getId();
            }
        } catch (Exception ignored) {
        }

        List<SeatResponse> seats = concertService.getConcertSeats(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Lấy sơ đồ ghế thành công", seats));
    }
}
