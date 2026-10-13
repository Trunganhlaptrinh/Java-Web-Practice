package com.concert.booking.controller;

import com.concert.booking.dto.request.ApplyVoucherRequest;
import com.concert.booking.dto.request.HoldSeatsRequest;
import com.concert.booking.dto.request.PaymentRequest;
import com.concert.booking.dto.response.ApiResponse;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.model.User;
import com.concert.booking.service.AuthService;
import com.concert.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final AuthService authService;

    @PostMapping("/hold")
    public ResponseEntity<ApiResponse<BookingResponse>> holdSeats(@Valid @RequestBody HoldSeatsRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        BookingResponse response = bookingService.holdSeats(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Khóa giữ chỗ thành công! Vui lòng thanh toán trong vòng 10 phút.", response));
    }

        @PostMapping("/voucher")
    public ResponseEntity<ApiResponse<BookingResponse>> applyVoucher(@Valid @RequestBody ApplyVoucherRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        BookingResponse response = bookingService.applyVoucher(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Áp dụng mã giảm giá thành công!", response));
    }

    
    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<BookingResponse>> processPaymentById(
            @PathVariable("id") String id,
            @RequestParam(value = "method", required = false, defaultValue = "VNPAY_QR") String method) {
        User user = authService.getCurrentAuthenticatedUser();
        PaymentRequest request = PaymentRequest.builder()
                .bookingId(id)
                .paymentMethod(method != null && !method.isBlank() ? method : "VNPAY_QR")
                .build();
        BookingResponse response = bookingService.processPayment(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Thanh toán thành công! Vé điện tử đã được phát hành.", response));
    }

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<BookingResponse>> processPayment(@Valid @RequestBody PaymentRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        BookingResponse response = bookingService.processPayment(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Thanh toán thành công! Vé điện tử đã được phát hành.", response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        User user = authService.getCurrentAuthenticatedUser();
        List<BookingResponse> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy lịch sử đơn đặt vé thành công", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingDetail(@PathVariable String id) {
        User user = authService.getCurrentAuthenticatedUser();
        BookingResponse booking = bookingService.getBookingDetail(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đơn đặt vé thành công", booking));
    }
}
