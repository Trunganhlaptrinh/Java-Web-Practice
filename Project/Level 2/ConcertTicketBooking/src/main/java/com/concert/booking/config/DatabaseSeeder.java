package com.concert.booking.config;

import com.concert.booking.model.*;
import com.concert.booking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final VoucherRepository voucherRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedVouchers();
        seedConcertsAndSeats();
    }

    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@concert.vn")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản trị viên Hệ thống")
                    .phone("0988888888")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info(">>> Seeded default ADMIN: admin / admin123 <<<");
        }

        User customer = userRepository.findByUsername("customer").orElse(null);
        if (customer == null) {
            customer = User.builder()
                    .username("customer")
                    .email("customer@gmail.com")
                    .password(passwordEncoder.encode("customer123"))
                    .fullName("Nguyễn Văn A")
                    .phone("0912345678")
                    .role(Role.CUSTOMER)
                    .active(true)
                    .build();
            userRepository.save(customer);
            log.info(">>> Seeded default CUSTOMER: customer (Nguyễn Văn A) / customer123 <<<");
        } else if (!"Nguyễn Văn A".equals(customer.getFullName())) {
            customer.setFullName("Nguyễn Văn A");
            userRepository.save(customer);
            log.info(">>> Updated CUSTOMER full name to: Nguyễn Văn A <<<");
        }
    }

    private void seedVouchers() {
        if (!voucherRepository.existsByCodeIgnoreCase("ANHTRAI10")) {
            Voucher v1 = Voucher.builder()
                    .code("ANHTRAI10")
                    .discountType(DiscountType.PERCENTAGE)
                    .discountValue(BigDecimal.valueOf(10))
                    .minOrderAmount(BigDecimal.valueOf(1000000))
                    .maxUsage(100)
                    .usedCount(0)
                    .active(true)
                    .startDate(Instant.now().minus(1, ChronoUnit.DAYS))
                    .endDate(Instant.now().plus(60, ChronoUnit.DAYS))
                    .build();
            voucherRepository.save(v1);
        }

        if (!voucherRepository.existsByCodeIgnoreCase("VIP50K")) {
            Voucher v2 = Voucher.builder()
                    .code("VIP50K")
                    .discountType(DiscountType.FIXED_AMOUNT)
                    .discountValue(BigDecimal.valueOf(50000))
                    .minOrderAmount(BigDecimal.valueOf(500000))
                    .maxUsage(200)
                    .usedCount(0)
                    .active(true)
                    .startDate(Instant.now().minus(1, ChronoUnit.DAYS))
                    .endDate(Instant.now().plus(60, ChronoUnit.DAYS))
                    .build();
            voucherRepository.save(v2);
        }
    }

    private void seedConcertsAndSeats() {
        List<Concert> existing = concertRepository.findAll();
        boolean hasAllFour = existing.size() >= 4 && existing.stream().anyMatch(c -> c.getTitle().contains("Sơn Tùng"));
        if (hasAllFour) {
            return;
        }

        log.info(">>> Re-seeding database with 4 official concerts and seat layouts... <<<");
        concertRepository.deleteAll();
        seatRepository.deleteAll();

        // ---------------------------------------------------------
        // Concert 1: Anh Trai Say Hi Live Concert 2026
        // ---------------------------------------------------------
        List<TicketTier> tiers1 = new ArrayList<>();
        tiers1.add(TicketTier.builder().tierId("VIP").name("VIP Diamond").price(BigDecimal.valueOf(2500000)).color("#000000").totalCapacity(24).availableCount(24).build());
        tiers1.add(TicketTier.builder().tierId("GOLD").name("VIP Gold Zone").price(BigDecimal.valueOf(1800000)).color("#D97706").totalCapacity(24).availableCount(24).build());
        tiers1.add(TicketTier.builder().tierId("CAT1").name("Khán đài CAT 1").price(BigDecimal.valueOf(1200000)).color("#2563EB").totalCapacity(36).availableCount(36).build());
        tiers1.add(TicketTier.builder().tierId("GA").name("Khán đài GA").price(BigDecimal.valueOf(650000)).color("#059669").totalCapacity(36).availableCount(36).build());

        Concert c1 = Concert.builder()
                .title("Anh Trai Say Hi Live Concert 2026")
                .artist("HIEUTHUHAI, RHYDER, Quang Hùng MasterD, Anh Tú Atus, Negav")
                .venue("Sân vận động Quốc gia Mỹ Đình, Hà Nội")
                .eventDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .bannerUrl("/images/AnhTraiSayHi2_logo.jpg")
                .description("Đại nhạc hội bùng nổ của dàn Anh Trai với dàn âm thanh ánh sáng chuẩn quốc tế tại Hà Nội.")
                .status("OPEN_FOR_SALE")
                .tiers(tiers1)
                .build();
        Concert savedC1 = concertRepository.save(c1);

        generateSeatsForConcert(savedC1.getId(), new String[][]{
                {"A", "VIP"}, {"B", "VIP"},
                {"C", "GOLD"}, {"D", "GOLD"},
                {"E", "CAT1"}, {"F", "CAT1"}, {"G", "CAT1"},
                {"H", "GA"}, {"J", "GA"}, {"K", "GA"}
        }, 12);

        // ---------------------------------------------------------
        // Concert 2: Sơn Tùng M-TP x Red Bull - Đêm Nhạc Năng Lượng
        // ---------------------------------------------------------
        List<TicketTier> tiers2 = new ArrayList<>();
        tiers2.add(TicketTier.builder().tierId("VIP").name("Fanzone VIP").price(BigDecimal.valueOf(2200000)).color("#000000").totalCapacity(20).availableCount(20).build());
        tiers2.add(TicketTier.builder().tierId("CAT1").name("Khán đài A").price(BigDecimal.valueOf(1500000)).color("#D97706").totalCapacity(30).availableCount(30).build());
        tiers2.add(TicketTier.builder().tierId("CAT2").name("Khán đài B").price(BigDecimal.valueOf(950000)).color("#2563EB").totalCapacity(30).availableCount(30).build());

        Concert c2 = Concert.builder()
                .title("Sơn Tùng M-TP x Red Bull - Đêm Nhạc Năng Lượng")
                .artist("Sơn Tùng M-TP")
                .venue("Sân vận động Quân khu 7, TP. Hồ Chí Minh")
                .eventDate(Instant.now().plus(45, ChronoUnit.DAYS))
                .bannerUrl("/images/RedBull-son-tung.jpg")
                .description("Sân khấu bùng nổ năng lượng đỉnh cao cùng Sơn Tùng M-TP và Red Bull với những bản hit đình đám.")
                .status("OPEN_FOR_SALE")
                .tiers(tiers2)
                .build();
        Concert savedC2 = concertRepository.save(c2);

        generateSeatsForConcert(savedC2.getId(), new String[][]{
                {"A", "VIP"}, {"B", "VIP"},
                {"C", "CAT1"}, {"D", "CAT1"}, {"E", "CAT1"},
                {"F", "CAT2"}, {"G", "CAT2"}, {"H", "CAT2"}
        }, 10);

        // ---------------------------------------------------------
        // Concert 3: Sóng 26 - Đại Nhạc Hội Giao Thừa Đón Năm Mới
        // ---------------------------------------------------------
        List<TicketTier> tiers3 = new ArrayList<>();
        tiers3.add(TicketTier.builder().tierId("VIP").name("VIP Countdown").price(BigDecimal.valueOf(1600000)).color("#000000").totalCapacity(20).availableCount(20).build());
        tiers3.add(TicketTier.builder().tierId("PREMIUM").name("Premium Zone").price(BigDecimal.valueOf(1000000)).color("#D97706").totalCapacity(30).availableCount(30).build());
        tiers3.add(TicketTier.builder().tierId("GA").name("Standard GA").price(BigDecimal.valueOf(450000)).color("#059669").totalCapacity(30).availableCount(30).build());

        Concert c3 = Concert.builder()
                .title("Sóng 26 - Đại Nhạc Hội Giao Thừa Đón Năm Mới")
                .artist("Dàn Nghệ Sĩ All-Stars & Rapper hàng đầu Việt Nam")
                .venue("Quảng trường Thời Đại & Phố Đi Bộ Nguyễn Huệ")
                .eventDate(Instant.now().plus(60, ChronoUnit.DAYS))
                .bannerUrl("/images/song-26.jpeg")
                .description("Đại nhạc hội Countdown đón năm mới hoành tráng, quy tụ hàng loạt ngôi sao âm nhạc hot nhất năm.")
                .status("OPEN_FOR_SALE")
                .tiers(tiers3)
                .build();
        Concert savedC3 = concertRepository.save(c3);

        generateSeatsForConcert(savedC3.getId(), new String[][]{
                {"A", "VIP"}, {"B", "VIP"},
                {"C", "PREMIUM"}, {"D", "PREMIUM"}, {"E", "PREMIUM"},
                {"F", "GA"}, {"G", "GA"}, {"H", "GA"}
        }, 10);

        // ---------------------------------------------------------
        // Concert 4: V-Concert - Rực Rỡ Hòa Ca Đất Trời
        // ---------------------------------------------------------
        List<TicketTier> tiers4 = new ArrayList<>();
        tiers4.add(TicketTier.builder().tierId("VVIP").name("VVIP Diamond").price(BigDecimal.valueOf(3000000)).color("#000000").totalCapacity(16).availableCount(16).build());
        tiers4.add(TicketTier.builder().tierId("VIP").name("VIP Gold").price(BigDecimal.valueOf(1800000)).color("#D97706").totalCapacity(24).availableCount(24).build());
        tiers4.add(TicketTier.builder().tierId("STD").name("Khán đài Standard").price(BigDecimal.valueOf(750000)).color("#2563EB").totalCapacity(32).availableCount(32).build());

        Concert c4 = Concert.builder()
                .title("V-Concert - Rực Rỡ Hòa Ca Đất Trời")
                .artist("Hà Anh Tuấn, Mỹ Tâm, Đen Vâu, Vũ.")
                .venue("Trung tâm Hội nghị Quốc gia, Hà Nội")
                .eventDate(Instant.now().plus(75, ChronoUnit.DAYS))
                .bannerUrl("/images/v-concert.jpg")
                .description("Đêm hòa nhạc acoustic và giao hưởng đỉnh cao quy tụ những giọng ca truyền cảm xúc hàng đầu Việt Nam.")
                .status("OPEN_FOR_SALE")
                .tiers(tiers4)
                .build();
        Concert savedC4 = concertRepository.save(c4);

        generateSeatsForConcert(savedC4.getId(), new String[][]{
                {"A", "VVIP"}, {"B", "VVIP"},
                {"C", "VIP"}, {"D", "VIP"}, {"E", "VIP"},
                {"F", "STD"}, {"G", "STD"}, {"H", "STD"}, {"K", "STD"}
        }, 8);

        log.info(">>> Seeded 4 official concerts with photos and seat layouts successfully! <<<");
    }

    private void generateSeatsForConcert(String concertId, String[][] rowsWithTiers, int seatsPerRow) {
        List<Seat> seats = new ArrayList<>();
        for (String[] rowTier : rowsWithTiers) {
            String row = rowTier[0];
            String tierId = rowTier[1];

            for (int col = 1; col <= seatsPerRow; col++) {
                String seatNumber = String.format("%s-%02d", row, col);
                Seat seat = Seat.builder()
                        .concertId(concertId)
                        .tierId(tierId)
                        .row(row)
                        .col(col)
                        .seatNumber(seatNumber)
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }
}
