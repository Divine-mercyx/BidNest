package org.BidNest2.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String buyerId;
    private String sellerId;
    private String auctionItemId;
    private Double amount;
    private LocalDateTime transactionDate;

    public Transaction() {
        transactionDate = LocalDateTime.now();
    }
}
