package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vouchers")
public class Voucher {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code; // VÃ­ dá»¥: EARLYBIRD10, SKYTOURVIP

    private DiscountType discountType; // PERCENTAGE hoáº·c FIXED_AMOUNT

    private BigDecimal discountValue; // 10 (%) hoáº·c 100,000 (VND)

    private BigDecimal minOrderAmount; // ÄÆ¡n hÃ ng tá»‘i thiá»ƒu

    private int maxUsage; // Tá»•ng sá»‘ lÆ°á»£t dÃ¹ng tá»‘i Ä‘a

    @Builder.Default
    private int usedCount = 0; // Sá»‘ lÆ°á»£t Ä‘Ã£ dÃ¹ng

    private Instant startDate;

    private Instant endDate;

    @Builder.Default
    private boolean active = true;

    @CreatedDate
    private Instant createdAt;
}
