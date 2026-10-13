package com.concert.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "TÃªn Ä‘Äƒng nháº­p hoáº·c email khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String usernameOrEmail;

    @NotBlank(message = "Máº­t kháº©u khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String password;
}