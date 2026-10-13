package com.concert.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "concerts")
public class Concert {

    @Id
    private String id;

    private String title;

    private String artist;

    private String bannerUrl;

    private String description;

    private String venue; // VÃ­ dá»¥: SÃ¢n váº­n Ä‘á»™ng Má»¹ ÄÃ¬nh, HÃ  Ná»™i

    private Instant eventDate;

    @Builder.Default
    private String status = "OPEN_FOR_SALE"; // OPEN_FOR_SALE | SOLD_OUT | COMPLETED | CANCELLED

    @Builder.Default
    private List<TicketTier> tiers = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
