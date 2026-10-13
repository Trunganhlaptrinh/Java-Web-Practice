package com.concert.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldSeatsRequest {
    @NotBlank(message = "MÃ£ concert khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String concertId;

    @NotEmpty(message = "Danh sÃ¡ch gháº¿ chá»n khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    @Size(max = 4, message = "ChÃ­nh sÃ¡ch phÃ²ng chá»‘ng vÃ© chá»£ Ä‘en: Má»—i láº§n chá»‰ Ä‘Æ°á»£c Ä‘áº·t tá»‘i Ä‘a 4 vÃ©")
    private List<String> seatIds;
}