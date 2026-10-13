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
public class PaymentRequest {
    @NotBlank(message = "MÃ£ Ä‘Æ¡n hÃ ng khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String bookingId;

    @NotBlank(message = "PhÆ°Æ¡ng thá»©c thanh toÃ¡n khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
    private String paymentMethod;
}