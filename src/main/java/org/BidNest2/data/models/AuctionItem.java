package org.BidNest2.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDate;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItem {
    @Id
    private String id;
    private String title;
    private String description;
    private Double currentBidAmount;
    private String imageUrl;
    private String sellerId;
    private LocalDate auctionStartDate;
    private Duration duration;
    private String currentBidderId;
}
