package com.concert.booking.exception;

import org.springframework.http.HttpStatus;

public class BookingExpiredException extends AppException {
    public BookingExpiredException(String message) {
        super(message, HttpStatus.GONE);
    }
}