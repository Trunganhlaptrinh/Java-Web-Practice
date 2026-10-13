package com.concert.booking.service.impl;

import com.concert.booking.exception.BadRequestException;
import com.concert.booking.service.EmailService;
import com.concert.booking.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;

    // Cache lưu trữ mã OTP trong bộ nhớ (Email -> OtpEntry)
    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();

    private record OtpEntry(String code, Instant expireAt) {}

    @Override
    public String generateAndSendOtp(String targetEmail) {
        String cleanEmail = targetEmail.trim().toLowerCase();

        // 1. Tự sinh mã OTP 6 số ngẫu nhiên (100000 - 999999) do chính App tạo ra
        String otpCode = String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 1000000));
        
        // 2. Mã có hiệu lực đúng 5 phút
        Instant expireAt = Instant.now().plus(5, ChronoUnit.MINUTES);
        otpStorage.put(cleanEmail, new OtpEntry(otpCode, expireAt));

        log.info(">>> App tự sinh mã OTP 6 số: {} cho email: {}, hiệu lực 5 phút <<<", otpCode, cleanEmail);

        // 3. Nhờ Gmail làm 'người đưa thư' gửi đến email
        emailService.sendOtpEmail(cleanEmail, otpCode);

        return otpCode;
    }

    @Override
    public boolean verifyOtp(String targetEmail, String otpCode) {
        String cleanEmail = targetEmail.trim().toLowerCase();
        OtpEntry entry = otpStorage.get(cleanEmail);

        if (entry == null) {
            throw new BadRequestException("Không tìm thấy mã OTP nào được gửi tới email này!");
        }

        if (Instant.now().isAfter(entry.expireAt())) {
            otpStorage.remove(cleanEmail);
            throw new BadRequestException("Mã OTP đã hết hạn hiệu lực 5 phút! Vui lòng yêu cầu mã mới.");
        }

        if (!entry.code().equals(otpCode.trim())) {
            throw new BadRequestException("Mã OTP không chính xác. Vui lòng kiểm tra lại!");
        }

        // Xác thực thành công -> xóa mã khỏi bộ nhớ để chống replay
        otpStorage.remove(cleanEmail);
        log.info(">>> Xác thực OTP thành công cho email: {} <<<", cleanEmail);
        return true;
    }
}
