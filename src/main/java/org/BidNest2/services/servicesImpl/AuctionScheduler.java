package org.BidNest2.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.repositories.AuctionItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AuctionScheduler {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuctionItemRepo auctionItemRepo;

    @Scheduled(fixedRate = 60000)
    public void checkExpiredAuctions() {
        List<AuctionItem> allAuctions = auctionItemRepo.findAll();
        for (AuctionItem auctionItem : allAuctions) {
            LocalDateTime auctionEndDate = auctionItem.getAuctionStartDate().atStartOfDay().plus(auctionItem.getDuration());
            if (LocalDateTime.now().isAfter(auctionEndDate)) {
                try {
                    userService.finalizeAuction(auctionItem.getId());
                } catch (IllegalArgumentException e) {
                    log.info("error: {}", e.getMessage());
                }
            }
        }
    }
}
