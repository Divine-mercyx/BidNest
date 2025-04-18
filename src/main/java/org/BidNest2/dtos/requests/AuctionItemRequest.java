package org.BidNest2.dtos.requests;

import lombok.Data;
import org.BidNest2.data.models.AuctionItem;

@Data
public class AuctionItemRequest {
    String token;
    String id;
    AuctionItem auctionItem;
}
