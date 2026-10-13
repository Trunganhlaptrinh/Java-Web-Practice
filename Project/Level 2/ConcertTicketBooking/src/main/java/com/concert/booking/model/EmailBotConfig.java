package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_bot_configs")
public class EmailBotConfig {

    @Id
    private String id;

    private String botEmail; // Email cá»§a bot do admin nháº­p

    private String appPassword; // Máº­t kháº©u á»©ng dá»¥ng / OTP token cá»§a Gmail hoáº·c mail provider

    @Builder.Default
    private String smtpHost = "smtp.gmail.com";

    @Builder.Default
    private int smtpPort = 587;

    @Builder.Default
    private boolean active = true;

    private String updatedByAdminUsername;

    @LastModifiedDate
    private Instant updatedAt;
}
