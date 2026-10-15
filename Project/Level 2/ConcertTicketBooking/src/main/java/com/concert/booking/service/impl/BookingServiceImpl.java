package com.concert.booking.service.impl;

import com.concert.booking.dto.request.ApplyVoucherRequest;
import com.concert.booking.dto.request.HoldSeatsRequest;
import com.concert.booking.dto.request.PaymentRequest;
import com.concert.booking.dto.response.BookingItemDto;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.dto.response.TicketResponse;
import com.concert.booking.exception.*;
import com.concert.booking.model.*;
import com.concert.booking.repository.*;
import com.concert.booking.service.BookingService;
import com.concert.booking.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final MongoTemplate mongoTemplate;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Value("${app.booking.hold-timeout-minutes:10}")
    private int holdTimeoutMinutes;

    @Value("${app.booking.max-tickets-per-booking:4}")
    private int maxTicketsPerBooking;

    @Override
    public BookingResponse holdSeats(String userId, HoldSeatsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Concert concert = concertRepository.findById(request.getConcertId())
                .orElseThrow(() -> new ResourceNotFoundException("Concert", "id", request.getConcertId()));

        if ("CANCELLED".equalsIgnoreCase(concert.getStatus())) {
            throw new BadRequestException("Sá»± kiá»‡n nÃ y Ä‘Ã£ bá»‹ há»§y, khÃ´ng thá»ƒ Ä‘áº·t vÃ©!");
        }

        // ChÃ­nh sÃ¡ch chá»‘ng vÃ© chá»£ Ä‘en (Anti-scalping)
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new BadRequestException("Vui lÃ²ng chá»n Ã­t nháº¥t 1 gháº¿!");
        }

        if (request.getSeatIds().size() > maxTicketsPerBooking) {
            throw new BadRequestException("ChÃ­nh sÃ¡ch chá»‘ng vÃ© chá»£ Ä‘en: Má»—i láº§n Ä‘áº·t chá»‰ Ä‘Æ°á»£c chá»n tá»‘i Ä‘a " 
                    + maxTicketsPerBooking + " vÃ©!");
        }

        Instant now = Instant.now();
        Instant holdExpiresAt = now.plus(Duration.ofMinutes(holdTimeoutMinutes));

        Map<String, TicketTier> tierMap = Optional.ofNullable(concert.getTiers())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(TicketTier::getTierId, Function.identity(), (t1, t2) -> t1));

        List<Seat> successfullyLockedSeats = new ArrayList<>();

        // KHÃ“A GHáº¾ NGUYÃŠN Tá»¬ (ATOMIC SEAT LOCKING)
        for (String seatId : request.getSeatIds()) {
            // Cho phÃ©p lock náº¿u:
            // 1. Tráº¡ng thÃ¡i lÃ  AVAILABLE
            // 2. HOáº¶C tráº¡ng thÃ¡i lÃ  HELD nhÆ°ng Ä‘Ã£ quÃ¡ háº¡n (lazy release lock)
            Criteria availableOrExpiredCriteria = new Criteria().orOperator(
                    Criteria.where("status").is(SeatStatus.AVAILABLE),
                    new Criteria().andOperator(
                            Criteria.where("status").is(SeatStatus.HELD),
                            Criteria.where("holdExpiresAt").lt(now)
                    )
            );

            Query lockQuery = new Query(
                    new Criteria().andOperator(
                            Criteria.where("id").is(seatId),
                            Criteria.where("concertId").is(concert.getId()),
                            availableOrExpiredCriteria
                    )
            );

            Update lockUpdate = new Update()
                    .set("status", SeatStatus.HELD)
                    .set("heldByUserId", userId)
                    .set("holdExpiresAt", holdExpiresAt);

            Seat lockedSeat = mongoTemplate.findAndModify(
                    lockQuery,
                    lockUpdate,
                    FindAndModifyOptions.options().returnNew(true),
                    Seat.class
            );

            if (lockedSeat == null) {
                // Xung Ä‘á»™t gháº¿ (Race condition) -> Rollback toÃ n bá»™ gháº¿ Ä‘Ã£ lock trÆ°á»›c Ä‘Ã³ trong lÆ°á»£t nÃ y
                rollbackLockedSeats(successfullyLockedSeats, userId);

                Seat conflictingSeat = seatRepository.findById(seatId).orElse(null);
                String seatLabel = conflictingSeat != null ? conflictingSeat.getSeatNumber() : seatId;
                throw new SeatUnavailableException("Gháº¿ " + seatLabel + " Ä‘Ã£ cÃ³ ngÆ°á»i khÃ¡c chá»n hoáº·c Ä‘ang thanh toÃ¡n. Vui lÃ²ng chá»n gháº¿ khÃ¡c!");
            }

            successfullyLockedSeats.add(lockedSeat);
        }

        // Táº¡o BookingItem vÃ  tÃ­nh tá»•ng tiá»n
        List<BookingItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Seat seat : successfullyLockedSeats) {
            TicketTier tier = tierMap.get(seat.getTierId());
            BigDecimal price = tier != null ? tier.getPrice() : BigDecimal.ZERO;
            String tierName = tier != null ? tier.getName() : seat.getTierId();

            items.add(BookingItem.builder()
                    .seatId(seat.getId())
                    .seatNumber(seat.getSeatNumber())
                    .tierName(tierName)
                    .price(price)
                    .build());

            totalAmount = totalAmount.add(price);
        }

        String bookingId = "BKG-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Booking booking = Booking.builder()
                .id(bookingId)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .userFullName(user.getFullName())
                .concertId(concert.getId())
                .concertTitle(concert.getTitle())
                .items(items)
                .totalAmount(totalAmount)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(totalAmount)
                .status(BookingStatus.PENDING_PAYMENT)
                .holdExpiresAt(holdExpiresAt)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Khá»Ÿi táº¡o Ä‘Æ¡n Ä‘áº·t vÃ© thÃ nh cÃ´ng: id={}, user={}, total={}, seats={}", 
                savedBooking.getId(), user.getUsername(), totalAmount, request.getSeatIds().size());

        return mapToBookingResponse(savedBooking, concert);
    }

    @Override
    public BookingResponse applyVoucher(String userId, ApplyVoucherRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", request.getBookingId()));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedException("Báº¡n khÃ´ng cÃ³ quyá»n thao tÃ¡c trÃªn Ä‘Æ¡n hÃ ng nÃ y!");
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new BadRequestException("ÄÆ¡n hÃ ng khÃ´ng á»Ÿ tráº¡ng thÃ¡i chá» thanh toÃ¡n!");
        }

        Instant now = Instant.now();
        if (booking.getHoldExpiresAt() != null && booking.getHoldExpiresAt().isBefore(now)) {
            releaseBookingSeats(booking);
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new BookingExpiredException("ÄÆ¡n hÃ ng Ä‘Ã£ háº¿t háº¡n giá»¯ chá»— (10 phÃºt). Vui lÃ²ng Ä‘áº·t láº¡i vÃ© má»›i!");
        }

        Voucher voucher = voucherRepository.findByCodeIgnoreCaseAndActiveTrue(request.getVoucherCode().trim())
                .orElseThrow(() -> new BadRequestException("MÃ£ giáº£m giÃ¡ '" + request.getVoucherCode() + "' khÃ´ng tá»“n táº¡i hoáº·c Ä‘Ã£ háº¿t hiá»‡u lá»±c!"));

        if (voucher.getStartDate() != null && voucher.getStartDate().isAfter(now)) {
            throw new BadRequestException("MÃ£ giáº£m giÃ¡ chÆ°a Ä‘áº¿n ngÃ y Ã¡p dá»¥ng!");
        }

        if (voucher.getEndDate() != null && voucher.getEndDate().isBefore(now)) {
            throw new BadRequestException("MÃ£ giáº£m giÃ¡ Ä‘Ã£ háº¿t háº¡n sá»­ dá»¥ng!");
        }

        if (voucher.getMaxUsage() > 0 && voucher.getUsedCount() >= voucher.getMaxUsage()) {
            throw new BadRequestException("MÃ£ giáº£m giÃ¡ Ä‘Ã£ háº¿t lÆ°á»£t sá»­ dá»¥ng!");
        }

        if (voucher.getMinOrderAmount() != null && booking.getTotalAmount().compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new BadRequestException("ÄÆ¡n hÃ ng chÆ°a Ä‘áº¡t giÃ¡ trá»‹ tá»‘i thiá»ƒu " + voucher.getMinOrderAmount() + " Ä‘á»ƒ Ã¡p dá»¥ng mÃ£ giáº£m giÃ¡ nÃ y!");
        }

        BigDecimal discount = BigDecimal.ZERO;
        if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = booking.getTotalAmount()
                    .multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        } else if (voucher.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            discount = voucher.getDiscountValue().min(booking.getTotalAmount());
        }

        BigDecimal finalAmount = booking.getTotalAmount().subtract(discount).max(BigDecimal.ZERO);

        booking.setVoucherCode(voucher.getCode());
        booking.setDiscountAmount(discount);
        booking.setFinalAmount(finalAmount);

        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Ãp dá»¥ng voucher {} thÃ nh cÃ´ng cho Ä‘Æ¡n {}: giáº£m {}", voucher.getCode(), booking.getId(), discount);

        Concert concert = concertRepository.findById(booking.getConcertId()).orElse(null);
        return mapToBookingResponse(updatedBooking, concert);
    }

    @Override
    @Transactional
    public BookingResponse processPayment(String userId, PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", request.getBookingId()));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedException("Báº¡n khÃ´ng cÃ³ quyá»n thanh toÃ¡n Ä‘Æ¡n hÃ ng nÃ y!");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BadRequestException("ÄÆ¡n hÃ ng nÃ y Ä‘Ã£ Ä‘Æ°á»£c thanh toÃ¡n trÆ°á»›c Ä‘Ã³!");
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new BadRequestException("ÄÆ¡n hÃ ng khÃ´ng á»Ÿ tráº¡ng thÃ¡i há»£p lá»‡ Ä‘á»ƒ thanh toÃ¡n!");
        }

        Instant now = Instant.now();
        if (booking.getHoldExpiresAt() != null && booking.getHoldExpiresAt().isBefore(now)) {
            releaseBookingSeats(booking);
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new BookingExpiredException("ÄÆ¡n hÃ ng Ä‘Ã£ háº¿t háº¡n giá»¯ chá»— (10 phÃºt). Gháº¿ Ä‘Ã£ Ä‘Æ°á»£c giáº£i phÃ³ng!");
        }

        Concert concert = concertRepository.findById(booking.getConcertId())
                .orElseThrow(() -> new ResourceNotFoundException("Concert", "id", booking.getConcertId()));

        // 1. Cáº­p nháº­t gháº¿ tá»« HELD sang SOLD
        for (BookingItem item : booking.getItems()) {
            Query seatQuery = new Query(
                    new Criteria().andOperator(
                            Criteria.where("id").is(item.getSeatId()),
                            Criteria.where("heldByUserId").is(userId)
                    )
            );
            Update seatUpdate = new Update()
                    .set("status", SeatStatus.BOOKED)
                    .unset("heldByUserId")
                    .unset("holdExpiresAt");

            mongoTemplate.updateFirst(seatQuery, seatUpdate, Seat.class);
        }

        // 2. Sinh vÃ© Ä‘iá»‡n tá»­ (Tickets) cho tá»«ng gháº¿
        List<Ticket> generatedTickets = new ArrayList<>();
        for (BookingItem item : booking.getItems()) {
            String ticketId = "TCK-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            String qrToken = String.format("{\"ticketId\":\"%s\",\"bookingId\":\"%s\",\"seat\":\"%s\",\"concertId\":\"%s\"}", 
                    ticketId, booking.getId(), item.getSeatNumber(), booking.getConcertId());

            Ticket ticket = Ticket.builder()
                    .id(ticketId)
                    .bookingId(booking.getId())
                    .userId(userId)
                    .concertId(concert.getId())
                    .concertTitle(concert.getTitle())
                    .venue(concert.getVenue())
                    .eventDate(concert.getEventDate())
                    .tierName(item.getTierName())
                    .seatNumber(item.getSeatNumber())
                    .price(item.getPrice())
                    .qrCodeData(qrToken)
                    .status(TicketStatus.VALID)
                    .build();

            generatedTickets.add(ticketRepository.save(ticket));
        }

        // 3. Tăng usedCount nếu có dùng voucher
        if (booking.getVoucherCode() != null && !booking.getVoucherCode().isBlank()) {
            voucherRepository.findByCodeIgnoreCaseAndActiveTrue(booking.getVoucherCode()).ifPresent(v -> {
                v.setUsedCount(v.getUsedCount() + 1);
                voucherRepository.save(v);
            });
        }

        // 4. Cập nhật số lượng vé khả dụng trong các hạng vé của concert
        if (concert.getTiers() != null && booking.getItems() != null) {
            Map<String, Long> bookedTierCounts = booking.getItems().stream()
                    .filter(item -> item.getTierName() != null)
                    .collect(Collectors.groupingBy(BookingItem::getTierName, Collectors.counting()));
            for (TicketTier tier : concert.getTiers()) {
                Long count = bookedTierCounts.get(tier.getName());
                if (count != null) {
                    tier.setAvailableCount(Math.max(0, tier.getAvailableCount() - count.intValue()));
                }
            }
            concertRepository.save(concert);
        }

        // 5. Chuyển trạng thái đơn sang CONFIRMED
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentMethod(request.getPaymentMethod());
        Booking paidBooking = bookingRepository.save(booking);

        log.info("Thanh toÃ¡n thÃ nh cÃ´ng Ä‘Æ¡n hÃ ng {}: tá»•ng tiá»n {}, phÆ°Æ¡ng thá»©c {}, phÃ¡t hÃ nh {} vÃ©", 
                paidBooking.getId(), paidBooking.getFinalAmount(), paidBooking.getPaymentMethod(), generatedTickets.size());

        // 5. Gá»­i email xÃ¡c nháº­n (cháº¡y báº¥t Ä‘á»“ng bá»™ ngáº§m)
        try {
            emailService.sendBookingConfirmationEmail(paidBooking, generatedTickets);
        } catch (Exception e) {
            log.error("Lá»—i khi kÃ­ch hoáº¡t gá»­i email cho Ä‘Æ¡n {}: {}", paidBooking.getId(), e.getMessage());
        }

        return mapToBookingResponse(paidBooking, concert);
    }

    @Override
    public List<BookingResponse> getUserBookings(String userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream().map(b -> {
            Concert concert = concertRepository.findById(b.getConcertId()).orElse(null);
            return mapToBookingResponse(b, concert);
        }).collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingDetail(String userId, String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedException("Báº¡n khÃ´ng cÃ³ quyá»n xem Ä‘Æ¡n hÃ ng nÃ y!");
        }

        Concert concert = concertRepository.findById(booking.getConcertId()).orElse(null);
        return mapToBookingResponse(booking, concert);
    }

    @Override
    public List<TicketResponse> getUserTickets(String userId) {
        List<Ticket> tickets = ticketRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return tickets.stream().map(t -> TicketResponse.builder()
                .id(t.getId())
                .ticketCode(t.getId())
                .bookingCode(t.getBookingId())
                .concertTitle(t.getConcertTitle())
                .venue(t.getVenue())
                .concertStartTime(t.getEventDate())
                .seatCode(t.getSeatNumber())
                .tierName(t.getTierName())
                .price(t.getPrice())
                .qrCodeData(t.getQrCodeData())
                .status(t.getStatus())
                .issuedAt(t.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void releaseExpiredBookings() {
        Instant now = Instant.now();
        List<Booking> expiredBookings = bookingRepository.findByStatusAndHoldExpiresAtBefore(BookingStatus.PENDING_PAYMENT, now);

        if (expiredBookings.isEmpty()) {
            return;
        }

        log.info("PhÃ¡t hiá»‡n {} Ä‘Æ¡n hÃ ng quÃ¡ háº¡n thanh toÃ¡n, báº¯t Ä‘áº§u giáº£i phÃ³ng gháº¿...", expiredBookings.size());

        for (Booking booking : expiredBookings) {
            try {
                releaseBookingSeats(booking);
                booking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(booking);
                log.info("ÄÃ£ giáº£i phÃ³ng gháº¿ cho Ä‘Æ¡n quÃ¡ háº¡n: id={}, user={}", booking.getId(), booking.getUserEmail());
            } catch (Exception ex) {
                log.error("Lá»—i khi giáº£i phÃ³ng gháº¿ cho Ä‘Æ¡n {}: {}", booking.getId(), ex.getMessage());
            }
        }
    }

    private void releaseBookingSeats(Booking booking) {
        if (booking.getItems() == null) return;

        for (BookingItem item : booking.getItems()) {
            Query query = new Query(
                    new Criteria().andOperator(
                            Criteria.where("id").is(item.getSeatId()),
                            Criteria.where("heldByUserId").is(booking.getUserId()),
                            Criteria.where("status").is(SeatStatus.HELD)
                    )
            );

            Update update = new Update()
                    .set("status", SeatStatus.AVAILABLE)
                    .unset("heldByUserId")
                    .unset("holdExpiresAt");

            mongoTemplate.updateFirst(query, update, Seat.class);
        }
    }

    private void rollbackLockedSeats(List<Seat> lockedSeats, String userId) {
        for (Seat seat : lockedSeats) {
            Query rollbackQuery = new Query(
                    new Criteria().andOperator(
                            Criteria.where("id").is(seat.getId()),
                            Criteria.where("heldByUserId").is(userId)
                    )
            );
            Update rollbackUpdate = new Update()
                    .set("status", SeatStatus.AVAILABLE)
                    .unset("heldByUserId")
                    .unset("holdExpiresAt");

            mongoTemplate.updateFirst(rollbackQuery, rollbackUpdate, Seat.class);
        }
    }

    private BookingResponse mapToBookingResponse(Booking booking, Concert concert) {
        Instant now = Instant.now();
        long remainingHoldSeconds = 0;
        if (booking.getStatus() == BookingStatus.PENDING_PAYMENT && booking.getHoldExpiresAt() != null) {
            remainingHoldSeconds = Math.max(0, Duration.between(now, booking.getHoldExpiresAt()).getSeconds());
        }

        List<BookingItemDto> itemDtos = Optional.ofNullable(booking.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(it -> BookingItemDto.builder()
                        .seatId(it.getSeatId())
                        .seatCode(it.getSeatNumber())
                        .tierName(it.getTierName())
                        .price(it.getPrice())
                        .build())
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingCode(booking.getId())
                .concertId(booking.getConcertId())
                .concertTitle(booking.getConcertTitle())
                .venue(concert != null ? concert.getVenue() : "Äang cáº­p nháº­t")
                .concertStartTime(concert != null ? concert.getEventDate() : null)
                .totalAmount(booking.getTotalAmount())
                .discountAmount(booking.getDiscountAmount())
                .finalAmount(booking.getFinalAmount())
                .status(booking.getStatus())
                .holdExpiresAt(booking.getHoldExpiresAt())
                .remainingHoldSeconds(remainingHoldSeconds)
                .items(itemDtos)
                .voucherCode(booking.getVoucherCode())
                .paymentMethod(booking.getPaymentMethod())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}