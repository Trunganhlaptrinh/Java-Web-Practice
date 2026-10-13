package com.concert.booking.exception;

import org.springframework.http.HttpStatus;

public class SeatUnavailableException extends AppException {
    public SeatUnavailableException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}