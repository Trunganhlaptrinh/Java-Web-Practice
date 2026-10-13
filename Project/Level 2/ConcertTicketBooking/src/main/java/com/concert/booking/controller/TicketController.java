package com.concert.booking.controller;

import com.concert.booking.dto.response.ApiResponse;
import com.concert.booking.dto.response.TicketResponse;
import com.concert.booking.model.User;
import com.concert.booking.service.AuthService;
import com.concert.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final BookingService bookingService;
    private final AuthService authService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getMyTickets() {
        User user = authService.getCurrentAuthenticatedUser();
        List<TicketResponse> tickets = bookingService.getUserTickets(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách vé thành công", tickets));
    }
}
