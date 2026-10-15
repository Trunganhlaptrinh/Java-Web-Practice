package com.concert.booking.repository;

import com.concert.booking.model.EmailBotConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailBotConfigRepository extends MongoRepository<EmailBotConfig, String> {
    Optional<EmailBotConfig> findFirstByActiveTrueOrderByUpdatedAtDesc();
    Optional<EmailBotConfig> findFirstByOrderByUpdatedAtDesc();
}
