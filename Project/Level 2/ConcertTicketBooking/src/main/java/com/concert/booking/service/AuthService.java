package com.concert.booking.service;

import com.concert.booking.dto.request.LoginRequest;
import com.concert.booking.dto.request.RegisterRequest;
import com.concert.booking.dto.response.AuthResponse;
import com.concert.booking.model.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse socialLogin(com.concert.booking.dto.request.SocialLoginRequest request);
    User getCurrentAuthenticatedUser();
}