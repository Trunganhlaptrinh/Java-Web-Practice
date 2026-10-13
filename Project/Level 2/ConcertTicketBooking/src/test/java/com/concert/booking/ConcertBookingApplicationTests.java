package com.concert.booking;

import com.concert.booking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConcertBookingApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(userRepository);
        System.out.println("====================================================");
        System.out.println(">>> SPRING BOOT & MONGODB REPOSITORIES LOADED OK! <<<");
        System.out.println("====================================================");
    }
}