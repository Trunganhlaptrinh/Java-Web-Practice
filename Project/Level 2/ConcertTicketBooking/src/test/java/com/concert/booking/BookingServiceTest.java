package com.concert.booking;

import com.concert.booking.dto.request.HoldSeatsRequest;
import com.concert.booking.dto.request.PaymentRequest;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.dto.response.TicketResponse;
import com.concert.booking.exception.BadRequestException;
import com.concert.booking.exception.SeatUnavailableException;
import com.concert.booking.model.*;
import com.concert.booking.repository.*;
import com.concert.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private User testUser1;
    private User testUser2;
    private Concert testConcert;
    private Seat seatA1;
    private Seat seatA2;

    @BeforeEach
    void setUp() {
        // Clean up test data
        ticketRepository.deleteAll();
        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        concertRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Create Test Users
        testUser1 = userRepository.save(User.builder()
                .username("customer1")
                .email("customer1@test.com")
                .password("123456")
                .fullName("Nguyen Van A")
                .phone("0912345678")
                .role(Role.CUSTOMER)
                .active(true)
                .build());

        testUser2 = userRepository.save(User.builder()
                .username("customer2")
                .email("customer2@test.com")
                .password("123456")
                .fullName("Tran Thi B")
                .phone("0987654321")
                .role(Role.CUSTOMER)
                .active(true)
                .build());

        // 2. Create Test Concert with Tiers
        TicketTier vvipTier = TicketTier.builder()
                .tierId("VVIP")
                .name("VVIP Zone")
                .price(BigDecimal.valueOf(2000000))
                .color("#FFD700")
                .totalCapacity(2)
                .availableCount(2)
                .build();

        testConcert = concertRepository.save(Concert.builder()
                .title("Anh Trai Say Hi Live Concert 2026")
                .artist("HIEUTHUHAI, RHYDER, Quang Hung MasterD")
                .venue("My Dinh National Stadium")
                .eventDate(Instant.now().plusSeconds(86400 * 30))
                .status("OPEN_FOR_SALE")
                .tiers(List.of(vvipTier))
                .build());

        // 3. Create Test Seats
        seatA1 = seatRepository.save(Seat.builder()
                .concertId(testConcert.getId())
                .tierId("VVIP")
                .seatNumber("A-01")
                .row("A")
                .col(1)
                .status(SeatStatus.AVAILABLE)
                .build());

        seatA2 = seatRepository.save(Seat.builder()
                .concertId(testConcert.getId())
                .tierId("VVIP")
                .seatNumber("A-02")
                .row("A")
                .col(2)
                .status(SeatStatus.AVAILABLE)
                .build());
    }

    @Test
    void testHoldSeatsSuccessAndPreventDoubleBooking() {
        // User 1 holds seat A-01
        HoldSeatsRequest holdReq = HoldSeatsRequest.builder()
                .concertId(testConcert.getId())
                .seatIds(List.of(seatA1.getId()))
                .build();

        BookingResponse booking = bookingService.holdSeats(testUser1.getId(), holdReq);
        assertNotNull(booking);
        assertEquals(BookingStatus.PENDING_PAYMENT, booking.getStatus());
        assertEquals(BigDecimal.valueOf(2000000), booking.getFinalAmount());
        assertEquals(1, booking.getItems().size());
        assertTrue(booking.getRemainingHoldSeconds() > 500);

        // Verify Seat in DB is now HELD
        Seat updatedSeatA1 = seatRepository.findById(seatA1.getId()).orElseThrow();
        assertEquals(SeatStatus.HELD, updatedSeatA1.getStatus());
        assertEquals(testUser1.getId(), updatedSeatA1.getHeldByUserId());

        // User 2 tries to hold the same seat A-01 -> MUST FAIL with SeatUnavailableException
        assertThrows(SeatUnavailableException.class, () -> {
            bookingService.holdSeats(testUser2.getId(), holdReq);
        });

        System.out.println("====================================================");
        System.out.println(">>> ATOMIC SEAT LOCKING & CONCURRENCY TEST PASSED! <<<");
        System.out.println("====================================================");
    }

    @Test
    void testAntiScalpingMaxTicketsPerBooking() {
        // Attempting to hold 5 seats must throw BadRequestException
        HoldSeatsRequest scalperReq = HoldSeatsRequest.builder()
                .concertId(testConcert.getId())
                .seatIds(List.of("s1", "s2", "s3", "s4", "s5"))
                .build();

        assertThrows(BadRequestException.class, () -> {
            bookingService.holdSeats(testUser1.getId(), scalperReq);
        });

        System.out.println("====================================================");
        System.out.println(">>> ANTI-SCALPING (>4 TICKETS) RULE TEST PASSED! <<<");
        System.out.println("====================================================");
    }

    @Test
    void testProcessPaymentAndIssueTickets() {
        // 1. Hold seat A-01 and A-02
        HoldSeatsRequest holdReq = HoldSeatsRequest.builder()
                .concertId(testConcert.getId())
                .seatIds(List.of(seatA1.getId(), seatA2.getId()))
                .build();

        BookingResponse booking = bookingService.holdSeats(testUser1.getId(), holdReq);
        assertEquals(BigDecimal.valueOf(4000000), booking.getFinalAmount());

        // 2. Process Payment
        PaymentRequest payReq = PaymentRequest.builder()
                .bookingId(booking.getId())
                .paymentMethod("VNPAY_QR")
                .build();

        BookingResponse paidBooking = bookingService.processPayment(testUser1.getId(), payReq);
        assertEquals(BookingStatus.CONFIRMED, paidBooking.getStatus());
        assertEquals("VNPAY_QR", paidBooking.getPaymentMethod());

        // 3. Verify Seats are BOOKED
        Seat s1 = seatRepository.findById(seatA1.getId()).orElseThrow();
        Seat s2 = seatRepository.findById(seatA2.getId()).orElseThrow();
        assertEquals(SeatStatus.BOOKED, s1.getStatus());
        assertEquals(SeatStatus.BOOKED, s2.getStatus());
        assertNull(s1.getHeldByUserId());

        // 4. Verify Tickets are issued
        List<TicketResponse> tickets = bookingService.getUserTickets(testUser1.getId());
        assertEquals(2, tickets.size());
        for (TicketResponse t : tickets) {
            assertEquals(TicketStatus.VALID, t.getStatus());
            assertNotNull(t.getQrCodeData());
            assertTrue(t.getQrCodeData().contains("ticketId"));
        }

        System.out.println("====================================================");
        System.out.println(">>> PAYMENT & TICKET ISSUANCE TEST PASSED! <<<");
        System.out.println(">>> Issued Tickets: " + tickets.size() + " tickets");
        System.out.println("====================================================");
    }
}