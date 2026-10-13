package com.concert.booking.repository;

import com.concert.booking.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Ticket> findByBookingId(String bookingId);
    Optional<Ticket> findByQrCodeData(String qrCodeData);
    long countByConcertId(String concertId);
}
