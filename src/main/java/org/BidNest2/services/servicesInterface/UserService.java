package org.BidNest2.services.servicesInterface;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.dtos.requests.AuctionItemRequest;
import org.BidNest2.dtos.requests.GetAuctionItemsRequest;

import java.util.List;

public interface UserService {
    AuctionItem auctionItem(AuctionItemRequest auctionItemRequest);
    void deleteAll();
    List<AuctionItem> getAllAuctionItems(GetAuctionItemsRequest request);
}
