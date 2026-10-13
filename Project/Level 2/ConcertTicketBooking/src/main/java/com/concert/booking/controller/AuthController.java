package com.concert.booking.controller;

import com.concert.booking.dto.request.LoginRequest;
import com.concert.booking.dto.request.OtpRequest;
import com.concert.booking.dto.request.RegisterRequest;
import com.concert.booking.dto.request.SocialLoginRequest;
import com.concert.booking.dto.response.ApiResponse;
import com.concert.booking.dto.response.AuthResponse;
import com.concert.booking.model.User;
import com.concert.booking.service.AuthService;
import com.concert.booking.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký tài khoản thành công!", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", response));
    }

    @PostMapping("/social-login")
    public ResponseEntity<ApiResponse<AuthResponse>> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        AuthResponse response = authService.socialLogin(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập bằng " + request.getProvider() + " thành công!", response));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Map<String, String>>> sendOtp(@Valid @RequestBody OtpRequest request) {
        String otpCode = otpService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Mã OTP 6 số (hiệu lực 5 phút) đã được tạo và gửi tới email của bạn!", Map.of(
                "email", request.getEmail(),
                "otpCode", otpCode
        )));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@Valid @RequestBody OtpRequest request) {
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok(ApiResponse.success("Xác thực mã OTP thành công!", valid));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = authService.getCurrentAuthenticatedUser();
        user.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công", user));
    }

    @Value("${app.oauth2.google.client-id:}")
    private String googleClientId;

    @Value("${app.oauth2.facebook.app-id:}")
    private String facebookAppId;

    @GetMapping("/oauth2/config")
    public ResponseEntity<ApiResponse<Map<String, String>>> getOAuth2Config() {
        return ResponseEntity.ok(ApiResponse.success("Lấy cấu hình OAuth2 thành công", Map.of(
                "googleClientId", googleClientId != null ? googleClientId : "",
                "facebookAppId", facebookAppId != null ? facebookAppId : ""
        )));
    }

}
