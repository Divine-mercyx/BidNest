package org.BidNest2.services.servicesInterface;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.models.Subscribe;
import org.BidNest2.dtos.requests.*;

import java.util.List;

public interface UserService {
    AuctionItem auctionItem(AuctionItemRequest auctionItemRequest);
    void deleteAll();
    List<AuctionItem> getAllAuctionItems(GetAuctionItemsRequest request);
    Subscribe subscribe(SubscribeRequest request);

    void unSubscribe(UnsubscribeRequest request);

    void bid(BIdRequest request);
}
