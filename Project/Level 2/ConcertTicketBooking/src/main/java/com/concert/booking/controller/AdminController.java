package com.concert.booking.controller;

import com.concert.booking.dto.request.EmailBotConfigRequest;
import com.concert.booking.dto.response.ApiResponse;
import com.concert.booking.dto.response.EmailBotConfigResponse;
import com.concert.booking.model.Booking;
import com.concert.booking.model.User;
import com.concert.booking.model.Voucher;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.TicketRepository;
import com.concert.booking.repository.VoucherRepository;
import com.concert.booking.service.AuthService;
import com.concert.booking.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final EmailService emailService;
    private final com.concert.booking.service.OtpService otpService;
    private final AuthService authService;
    private final VoucherRepository voucherRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    @PostMapping("/email-bot")
    public ResponseEntity<ApiResponse<EmailBotConfigResponse>> saveEmailBotConfig(@Valid @RequestBody EmailBotConfigRequest request) {
        User admin = authService.getCurrentAuthenticatedUser();
        EmailBotConfigResponse response = emailService.saveEmailBotConfig(admin.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Lưu cấu hình Email Bot gửi xác thực tự động thành công!", response));
    }

    @GetMapping("/email-bot")
    public ResponseEntity<ApiResponse<EmailBotConfigResponse>> getEmailBotConfig() {
        EmailBotConfigResponse response = emailService.getEmailBotConfig();
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin cấu hình Email Bot thành công", response));
    }

    @PostMapping("/vouchers")
    public ResponseEntity<ApiResponse<Voucher>> createVoucher(@RequestBody Voucher voucher) {
        Voucher saved = voucherRepository.save(voucher);
        return ResponseEntity.ok(ApiResponse.success("Tạo mã giảm giá thành công!", saved));
    }

    @GetMapping("/vouchers")
    public ResponseEntity<ApiResponse<List<Voucher>>> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách mã giảm giá thành công", vouchers));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn hàng toàn hệ thống thành công", bookings));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        List<Booking> bookings = bookingRepository.findAll();
        long totalBookings = bookings.size();

        BigDecimal totalRevenue = bookings.stream()
                .filter(b -> b.getFinalAmount() != null)
                .map(Booking::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalTickets = ticketRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalTicketsIssued", totalTickets);

        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê hệ thống thành công", stats));
    }

    @PostMapping("/email-bot/send-test-otp")
    public ResponseEntity<ApiResponse<Map<String, String>>> sendTestOtp(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "").trim();
        if (email.isEmpty()) {
            throw new com.concert.booking.exception.BadRequestException("Email nhận không được để trống!");
        }
        String otp = otpService.generateAndSendOtp(email);
        return ResponseEntity.ok(ApiResponse.success("Đã sinh mã OTP 6 số do App tạo (hiệu lực 5 phút) và chuyển cho Gmail Bot gửi!", Map.of(
                "email", email,
                "otpCode", otp,
                "expiresIn", "5 phút",
                "generator", "Hệ thống THE SHOW (tự sinh ngẫu nhiên từ 100000 - 999999)"
        )));
    }

    @PostMapping("/email-bot/verify-test-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyTestOtp(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "").trim();
        String code = body.getOrDefault("code", "").trim();
        boolean ok = otpService.verifyOtp(email, code);
        return ResponseEntity.ok(ApiResponse.success("Xác minh mã OTP thành công!", ok));
    }

}
