package com.concert.booking;

import com.concert.booking.model.Role;
import com.concert.booking.model.User;
import com.concert.booking.security.CustomUserDetails;
import com.concert.booking.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testGenerateAndValidateJwtToken() {
        User mockUser = User.builder()
                .id("test-user-id-123")
                .username("testuser")
                .email("testuser@gmail.com")
                .fullName("Test User")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        // 1. Generate Token
        String token = jwtTokenProvider.generateTokenFromUserDetails(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 20);

        // 2. Validate Token
        assertTrue(jwtTokenProvider.validateToken(token));

        // 3. Extract Claims
        assertEquals("testuser", jwtTokenProvider.getUsernameFromToken(token));
        assertEquals("test-user-id-123", jwtTokenProvider.getUserIdFromToken(token));

        System.out.println("====================================================");
        System.out.println(">>> JWT TOKEN GENERATION & VALIDATION TEST PASSED! <<<");
        System.out.println(">>> Generated Token: " + token.substring(0, 30) + "...");
        System.out.println("====================================================");
    }
}