package org.BidNest2.data.repositories;

import org.BidNest2.data.models.AuctionItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuctionItemRepo extends MongoRepository<AuctionItem, String> {
    List<AuctionItem> findBySellerId(String sellerId);
    List<AuctionItem> findAllByAuctionStartDate(LocalDate auctionStartDate);
}
