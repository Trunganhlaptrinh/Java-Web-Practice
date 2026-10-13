package com.concert.booking.dto.response;

import com.concert.booking.model.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private String id;
    private String concertId;
    private String tierId;
    private String tierName;
    private String tierColor;
    private String row;
    private int seatNumber;
    private String seatCode;
    private BigDecimal price;
    private SeatStatus status;
    private boolean isHeldByCurrentUser;
}