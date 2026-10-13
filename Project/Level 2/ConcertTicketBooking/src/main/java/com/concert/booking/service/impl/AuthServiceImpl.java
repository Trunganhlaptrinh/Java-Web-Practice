package com.concert.booking.service.impl;

import com.concert.booking.dto.request.LoginRequest;
import com.concert.booking.dto.request.RegisterRequest;
import com.concert.booking.dto.response.AuthResponse;
import com.concert.booking.exception.BadRequestException;
import com.concert.booking.exception.UnauthorizedException;
import com.concert.booking.model.Role;
import com.concert.booking.model.User;
import com.concert.booking.repository.UserRepository;
import com.concert.booking.security.CustomUserDetails;
import com.concert.booking.security.JwtTokenProvider;
import com.concert.booking.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("TÃªn Ä‘Äƒng nháº­p '" + request.getUsername() + "' Ä‘Ã£ Ä‘Æ°á»£c sá»­ dá»¥ng!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' Ä‘Ã£ Ä‘Æ°á»£c Ä‘Äƒng kÃ½!");
        }

        User newUser = User.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .phone(request.getPhone().trim())
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("NgÆ°á»i dÃ¹ng má»›i Ä‘Äƒng kÃ½ thÃ nh cÃ´ng: username={}, email={}", savedUser.getUsername(), savedUser.getEmail());

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtTokenProvider.generateTokenFromUserDetails(userDetails);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getUsernameOrEmail().trim();

        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier.toLowerCase()))
                .orElseThrow(() -> new BadRequestException("TÃªn Ä‘Äƒng nháº­p hoáº·c máº­t kháº©u khÃ´ng chÃ­nh xÃ¡c!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("TÃªn Ä‘Äƒng nháº­p hoáº·c máº­t kháº©u khÃ´ng chÃ­nh xÃ¡c!");
        }

        if (!user.isActive()) {
            throw new BadRequestException("TÃ i khoáº£n cá»§a báº¡n Ä‘Ã£ bá»‹ khÃ³a. Vui lÃ²ng liÃªn há»‡ quáº£n trá»‹ viÃªn!");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtTokenProvider.generateTokenFromUserDetails(userDetails);

        log.info("NgÆ°á»i dÃ¹ng Ä‘Äƒng nháº­p thÃ nh cÃ´ng: username={}", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("Vui lÃ²ng Ä‘Äƒng nháº­p Ä‘á»ƒ thá»±c hiá»‡n chá»©c nÄƒng nÃ y!");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("KhÃ´ng tÃ¬m tháº¥y thÃ´ng tin tÃ i khoáº£n ngÆ°á»i dÃ¹ng hiá»‡n táº¡i!"));
    }

    @Override
    @Transactional
    public AuthResponse socialLogin(com.concert.booking.dto.request.SocialLoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String fullName = request.getFullName().trim();
        String provider = request.getProvider().toUpperCase();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Tự động đăng ký người dùng mới từ Google/Facebook
            String baseUsername = email.contains("@") ? email.substring(0, email.indexOf("@")) : "user";
            String username = baseUsername;
            int counter = 1;
            while (userRepository.existsByUsername(username)) {
                username = baseUsername + counter++;
            }

            user = User.builder()
                    .username(username)
                    .email(email)
                    .fullName(fullName.isEmpty() ? "Người dùng " + provider : fullName)
                    .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                    .role(Role.CUSTOMER)
                    .active(true)
                    .phone("0900000000")
                    .build();

            user = userRepository.save(user);
            log.info(">>> Đã tự động tạo tài khoản mới từ {}: username={}, email={} <<<", provider, user.getUsername(), user.getEmail());
        }

        if (!user.isActive()) {
            throw new BadRequestException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên!");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtTokenProvider.generateTokenFromUserDetails(userDetails);

        log.info(">>> Người dùng đăng nhập thành công qua {}: username={}, email={} <<<", provider, user.getUsername(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

}
