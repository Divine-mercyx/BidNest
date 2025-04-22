package org.BidNest2.dtos.requests;

import lombok.Data;

@Data
public class BidRequest {
    String bidderId;
    String token;
    String itemId;
    double amount;
}
