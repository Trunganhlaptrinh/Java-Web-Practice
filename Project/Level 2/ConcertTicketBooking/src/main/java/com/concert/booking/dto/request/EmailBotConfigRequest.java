package com.concert.booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailBotConfigRequest {
    @NotBlank(message = "Email bot khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    @Email(message = "Email khÃ´ng Ä‘Ãºng Ä‘á»‹nh dáº¡ng")
    private String email;

    @NotBlank(message = "Máº­t kháº©u á»©ng dá»¥ng (App Password) khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String appPassword;

    private String fromName;

    private boolean active;
}