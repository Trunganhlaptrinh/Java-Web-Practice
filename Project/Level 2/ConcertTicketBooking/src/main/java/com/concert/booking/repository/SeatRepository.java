package com.concert.booking.repository;

import com.concert.booking.model.Seat;
import com.concert.booking.model.SeatStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends MongoRepository<Seat, String> {
    List<Seat> findByConcertIdOrderByRowAscColAsc(String concertId);
    List<Seat> findByConcertIdAndTierId(String concertId, String tierId);
    Optional<Seat> findByConcertIdAndSeatNumber(String concertId, String seatNumber);
    List<Seat> findByStatusAndHoldExpiresAtBefore(SeatStatus status, Instant now);
    long countByConcertIdAndStatus(String concertId, SeatStatus status);
}
