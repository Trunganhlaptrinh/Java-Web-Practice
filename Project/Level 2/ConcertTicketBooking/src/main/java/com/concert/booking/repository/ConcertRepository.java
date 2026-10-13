package com.concert.booking.repository;

import com.concert.booking.model.Concert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends MongoRepository<Concert, String> {
    List<Concert> findAllByOrderByEventDateAsc();
    List<Concert> findByStatusOrderByEventDateAsc(String status);
}
