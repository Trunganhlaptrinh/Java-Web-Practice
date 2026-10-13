package com.concert.booking.service.impl;

import com.concert.booking.dto.request.EmailBotConfigRequest;
import com.concert.booking.dto.response.EmailBotConfigResponse;
import com.concert.booking.model.Booking;
import com.concert.booking.model.EmailBotConfig;
import com.concert.booking.model.Ticket;
import com.concert.booking.repository.EmailBotConfigRepository;
import com.concert.booking.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailBotConfigRepository emailBotConfigRepository;

    @Override
    @Async
    public void sendBookingConfirmationEmail(Booking booking, List<Ticket> tickets) {
        Optional<EmailBotConfig> configOpt = emailBotConfigRepository.findFirstByActiveTrueOrderByUpdatedAtDesc();

        if (configOpt.isPresent() && configOpt.get().getAppPassword() != null && !configOpt.get().getAppPassword().isBlank()) {
            EmailBotConfig config = configOpt.get();
            try {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost(config.getSmtpHost() != null ? config.getSmtpHost() : "smtp.gmail.com");
                mailSender.setPort(config.getSmtpPort() > 0 ? config.getSmtpPort() : 587);
                mailSender.setUsername(config.getBotEmail());
                mailSender.setPassword(config.getAppPassword());

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "false");

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                String fromName = "Concert Booking System";
                helper.setFrom(config.getBotEmail(), fromName);
                helper.setTo(booking.getUserEmail());
                helper.setSubject("ðŸŽŸï¸ XÃ¡c nháº­n Ä‘áº·t vÃ© thÃ nh cÃ´ng: " + booking.getConcertTitle() + " [MÃ£ Ä‘Æ¡n #" + booking.getId() + "]");

                String htmlContent = buildHtmlContent(booking, tickets);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                log.info(">>> ÄÃ£ gá»­i email xÃ¡c nháº­n thÃ nh cÃ´ng tá»›i {} qua Bot Email: {} <<<", booking.getUserEmail(), config.getBotEmail());
                return;
            } catch (Exception ex) {
                log.error(">>> KhÃ´ng thá»ƒ gá»­i email qua SMTP Bot ({}): {}. Chuyá»ƒn sang ghi log MOCK! <<<", config.getBotEmail(), ex.getMessage());
            }
        }

        // Cháº¿ Ä‘á»™ Mock Console náº¿u admin chÆ°a cáº¥u hÃ¬nh hoáº·c cáº¥u hÃ¬nh lá»—i
        logMockEmail(booking, tickets);
    }

    @Override
    public EmailBotConfigResponse saveEmailBotConfig(String adminUsername, EmailBotConfigRequest request) {
        EmailBotConfig config = emailBotConfigRepository.findFirstByActiveTrueOrderByUpdatedAtDesc()
                .orElse(new EmailBotConfig());

        config.setBotEmail(request.getEmail().trim());
        config.setAppPassword(request.getAppPassword().trim());
        config.setActive(request.isActive());
        config.setUpdatedByAdminUsername(adminUsername);
        config.setUpdatedAt(Instant.now());

        EmailBotConfig saved = emailBotConfigRepository.save(config);
        log.info("Admin [{}] Ä‘Ã£ cáº­p nháº­t cáº¥u hÃ¬nh Email Bot: {}", adminUsername, saved.getBotEmail());

        return EmailBotConfigResponse.builder()
                .id(saved.getId())
                .email(saved.getBotEmail())
                .fromName(request.getFromName() != null ? request.getFromName() : "Concert Booking System")
                .active(saved.isActive())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public EmailBotConfigResponse getEmailBotConfig() {
        return emailBotConfigRepository.findFirstByActiveTrueOrderByUpdatedAtDesc()
                .map(cfg -> EmailBotConfigResponse.builder()
                        .id(cfg.getId())
                        .email(cfg.getBotEmail())
                        .fromName("Concert Booking System")
                        .active(cfg.isActive())
                        .updatedAt(cfg.getUpdatedAt())
                        .build())
                .orElse(null);
    }

    private void logMockEmail(Booking booking, List<Ticket> tickets) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String amountStr = booking.getFinalAmount() != null ? currencyFormat.format(booking.getFinalAmount()) : "0 VND";

        log.info("====================================================================================");
        log.info(" [MOCK EMAIL SERVICE] (Admin chÆ°a cáº¥u hÃ¬nh Email Bot hoáº·c Ä‘ang á»Ÿ mÃ´i trÆ°á»ng Test)");
        log.info(" NgÆ°á»i nháº­n: {} <{}>", booking.getUserFullName(), booking.getUserEmail());
        log.info(" TiÃªu Ä‘á»: XÃ¡c nháº­n Ä‘áº·t vÃ© thÃ nh cÃ´ng - Concert: {}", booking.getConcertTitle());
        log.info(" MÃ£ Ä‘Æ¡n hÃ ng: {}", booking.getId());
        log.info(" Tá»•ng sá»‘ tiá»n thanh toÃ¡n: {}", amountStr);
        log.info(" PhÆ°Æ¡ng thá»©c thanh toÃ¡n: {}", booking.getPaymentMethod());
        log.info(" Danh sÃ¡ch vÃ© phÃ¡t hÃ nh ({} vÃ©):", tickets.size());
        for (Ticket t : tickets) {
            log.info("   + VÃ© [{}]: Gháº¿ {} | Háº¡ng: {} | QR: {}", t.getId(), t.getSeatNumber(), t.getTierName(), t.getQrCodeData());
        }
        log.info("====================================================================================");
    }

    private String buildHtmlContent(Booking booking, List<Ticket> tickets) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String amountStr = booking.getFinalAmount() != null ? currencyFormat.format(booking.getFinalAmount()) : "0 VND";

        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 8px;'>");
        sb.append("<h2 style='color: #4f46e5; text-align: center;'>ðŸŽ‰ Äáº¶T VÃ‰ CONCERT THÃ€NH CÃ”NG!</h2>");
        sb.append("<p>Xin chÃ o <strong>").append(booking.getUserFullName()).append("</strong>,</p>");
        sb.append("<p>ÄÆ¡n Ä‘áº·t vÃ© cá»§a báº¡n cho sá»± kiá»‡n <strong>").append(booking.getConcertTitle()).append("</strong> Ä‘Ã£ Ä‘Æ°á»£c xÃ¡c nháº­n thanh toÃ¡n thÃ nh cÃ´ng.</p>");
        
        sb.append("<div style='background-color: #f8fafc; padding: 15px; border-radius: 6px; margin: 15px 0;'>");
        sb.append("<p style='margin: 4px 0;'><strong>MÃ£ Ä‘Æ¡n hÃ ng:</strong> ").append(booking.getId()).append("</p>");
        sb.append("<p style='margin: 4px 0;'><strong>Tá»•ng tiá»n Ä‘Ã£ thanh toÃ¡n:</strong> ").append(amountStr).append("</p>");
        sb.append("<p style='margin: 4px 0;'><strong>PhÆ°Æ¡ng thá»©c:</strong> ").append(booking.getPaymentMethod()).append("</p>");
        sb.append("</div>");

        sb.append("<h3>Danh sÃ¡ch vÃ© Ä‘iá»‡n tá»­:</h3>");
        sb.append("<table style='width: 100%; border-collapse: collapse; margin-bottom: 20px;'>");
        sb.append("<tr style='background-color: #f1f5f9; text-align: left;'>");
        sb.append("<th style='padding: 8px; border: 1px solid #cbd5e1;'>MÃ£ vÃ©</th>");
        sb.append("<th style='padding: 8px; border: 1px solid #cbd5e1;'>Sá»‘ gháº¿</th>");
        sb.append("<th style='padding: 8px; border: 1px solid #cbd5e1;'>Háº¡ng vÃ©</th>");
        sb.append("</tr>");

        for (Ticket t : tickets) {
            sb.append("<tr>");
            sb.append("<td style='padding: 8px; border: 1px solid #cbd5e1;'>").append(t.getId()).append("</td>");
            sb.append("<td style='padding: 8px; border: 1px solid #cbd5e1; font-weight: bold; color: #e11d48;'>").append(t.getSeatNumber()).append("</td>");
            sb.append("<td style='padding: 8px; border: 1px solid #cbd5e1;'>").append(t.getTierName()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        sb.append("<p style='color: #64748b; font-size: 13px;'>* Vui lÃ²ng xuáº¥t trÃ¬nh mÃ£ vÃ© nÃ y khi tá»›i sá»± kiá»‡n.</p>");
        sb.append("<div style='text-align: center; color: #94a3b8; font-size: 12px; margin-top: 30px; border-top: 1px solid #e2e8f0; padding-top: 10px;'>Concert Ticket Booking System Â© 2026</div>");
        sb.append("</div>");

        return sb.toString();
    }

    @Override
    public boolean sendOtpEmail(String targetEmail, String otpCode) {
        EmailBotConfig config = emailBotConfigRepository.findFirstByActiveTrueOrderByUpdatedAtDesc().orElse(null);
        if (config != null && config.isActive() && config.getBotEmail() != null && config.getAppPassword() != null) {
            try {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost(config.getSmtpHost() != null ? config.getSmtpHost() : "smtp.gmail.com");
                mailSender.setPort(config.getSmtpPort() > 0 ? config.getSmtpPort() : 587);
                mailSender.setUsername(config.getBotEmail());
                mailSender.setPassword(config.getAppPassword());

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "false");

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(config.getBotEmail(), "THE SHOW Booking");
                helper.setTo(targetEmail);
                helper.setSubject("Mã xác thực OTP THE SHOW: " + otpCode);

                String html = "<div style='font-family: Arial, sans-serif; max-width: 520px; margin: 0 auto; padding: 24px; border: 1px solid #E5E7EB; border-radius: 12px; background: #FFFFFF;'>"
                        + "<div style='text-align: center; margin-bottom: 20px;'>"
                        + "<h2 style='color: #000000; font-weight: 900; margin: 0; font-size: 22px;'>THE SHOW</h2>"
                        + "<p style='color: #6B7280; font-size: 13px; margin-top: 4px;'>Hệ Thống Đặt Vé Concert Quốc Tế</p>"
                        + "</div>"
                        + "<p style='font-size: 14px; color: #111827;'>Xin chào,</p>"
                        + "<p style='font-size: 14px; color: #374151;'>Bạn vừa yêu cầu mã xác thực OTP từ hệ thống. Đây là mã số bảo mật do hệ thống THE SHOW tự sinh:</p>"
                        + "<div style='background: #F9FAFB; border: 1px dashed #000000; border-radius: 8px; padding: 18px; text-align: center; margin: 24px 0;'>"
                        + "<span style='font-size: 32px; font-weight: 900; letter-spacing: 8px; color: #000000; display: block;'>" + otpCode + "</span>"
                        + "<span style='font-size: 12px; color: #6B7280; display: block; margin-top: 6px;'>Hiệu lực trong vòng <strong>5 phút</strong></span>"
                        + "</div>"
                        + "<p style='font-size: 13px; color: #6B7280; line-height: 1.5;'>* Lưu ý: Không chia sẻ mã OTP này cho bất kỳ ai dưới mọi hình thức để bảo mật tài khoản.</p>"
                        + "<div style='border-top: 1px solid #E5E7EB; margin-top: 24px; padding-top: 12px; text-align: center; font-size: 12px; color: #9CA3AF;'>"
                        + "THE SHOW © 2026 · Trụ sở chính: Hà Nội, Việt Nam"
                        + "</div>"
                        + "</div>";

                helper.setText(html, true);
                mailSender.send(message);
                log.info(">>> Đã gửi email mã OTP ({}) tới {} qua Gmail Bot: {} <<<", otpCode, targetEmail, config.getBotEmail());
                return true;
            } catch (Exception ex) {
                log.error(">>> Gửi email qua SMTP Bot ({}) thất bại: {}. Ghi log MOCK! <<<", config.getBotEmail(), ex.getMessage());
            }
        }

        // Mock log
        log.info("====================================================================================");
        log.info(" [MOCK OTP EMAIL SENDER] (Bot Gmail chưa bật hoặc gửi trong môi trường Test)");
        log.info(" Người nhận: {}", targetEmail);
        log.info(" Mã OTP 6 số: {}", otpCode);
        log.info(" Hiệu lực: 5 phút");
        log.info("====================================================================================");
        return false;
    }

}
