package com.concert.booking.service;

import com.concert.booking.dto.request.ApplyVoucherRequest;
import com.concert.booking.dto.request.HoldSeatsRequest;
import com.concert.booking.dto.request.PaymentRequest;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.dto.response.TicketResponse;

import java.util.List;

public interface BookingService {
    BookingResponse holdSeats(String userId, HoldSeatsRequest request);
    BookingResponse applyVoucher(String userId, ApplyVoucherRequest request);
    BookingResponse processPayment(String userId, PaymentRequest request);
    List<BookingResponse> getUserBookings(String userId);
    BookingResponse getBookingDetail(String userId, String bookingId);
    List<TicketResponse> getUserTickets(String userId);
    void releaseExpiredBookings();
}