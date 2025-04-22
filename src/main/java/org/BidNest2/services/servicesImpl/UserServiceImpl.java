package org.BidNest2.services.servicesImpl;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.models.Subscribe;
import org.BidNest2.data.models.Transaction;
import org.BidNest2.data.models.User;
import org.BidNest2.data.repositories.AuctionItemRepo;
import org.BidNest2.data.repositories.SubscribeRepo;
import org.BidNest2.data.repositories.TransactionRepo;
import org.BidNest2.data.repositories.UserRepo;
import org.BidNest2.dtos.requests.*;
import org.BidNest2.services.servicesInterface.UserService;
import org.BidNest2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuctionItemRepo auctionItemRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SubscribeRepo subscribeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public AuctionItem auctionItem(AuctionItemRequest request) {
        validateToken(request.getToken(), request.getId());
        if (request.getToken() == null || request.getToken().isEmpty()) throw new IllegalArgumentException("token is empty");
        User seller = userRepo.findById(request.getId()).orElse(null);
        List<Subscribe> subscribes = subscribeRepo.findAllBySubscribedToId(seller.getId());
        messageSubscribers(subscribes, seller);
        return auctionItemRepo.save(request.getAuctionItem());
    }

    private void messageSubscribers(List<Subscribe> subscribes, User seller) {
        if (!subscribes.isEmpty()) {
            for (Subscribe subscribe : subscribes) {
                User subscriber = userRepo.findById(subscribe.getSubscriberId()).orElse(null);
                assert subscriber != null;
                assert seller != null;
                emailServiceImpl.sendSimpleMail(subscriber.getEmail(), "Auction Update", String.format("hi, %s %s has auctioned an item check it out!", seller.getProfile().getFirstName(), seller.getProfile().getLastName()));
            }
        }
    }

    @Override
    public void deleteAll() {
        auctionItemRepo.deleteAll();
    }

    @Override
    public List<AuctionItem> getAllAuctionItems(GetAuctionItemsRequest request) {
        validateToken(request.getToken(), request.getId());
        return auctionItemRepo.findBySellerId(request.getId());
    }

    private void validateToken(String token, String id) {
        if (!jwtUtils.validateToken(token, id)) throw new IllegalArgumentException("Invalid token");
    }

    @Override
    public Subscribe subscribe(SubscribeRequest request) {
        validateToken(request.getToken(), request.getSubscriberId());
        Subscribe subscribe = new Subscribe();
        subscribe.setSubscriberId(request.getSubscriberId());
        subscribe.setSubscribedToId(request.getSubscriberToId());
        return subscribeRepo.save(subscribe);
    }

    @Override
    public void unSubscribe(UnsubscribeRequest request) {
        subscribeRepo.deleteBySubscribedToIdAndSubscriberId(request.getSubscriberToId(), request.getSubscriberId());
    }

    @Override
    public AuctionItem bid(BidRequest request) {
        validateToken(request.getToken(), request.getBidderId());

        AuctionItem auctionItem = auctionItemRepo.findById(request.getItemId()).orElse(null);
        assert auctionItem != null;

        LocalDate auctionEndDate = auctionItem.getAuctionStartDate().plus(auctionItem.getDuration());
        validateAuction(request, auctionEndDate, auctionItem);

        auctionItem.setCurrentBidAmount(request.getAmount());
        auctionItem.setCurrentBidderId(request.getBidderId());
        return auctionItemRepo.save(auctionItem);
    }

    private static void validateAuction(BidRequest request, LocalDate auctionEndDate, AuctionItem auctionItem) {
        if (LocalDate.now().isAfter(auctionEndDate)) {
            throw new IllegalArgumentException("Cannot bid, auction time has passed.");
        }

        if (Objects.equals(auctionItem.getSellerId(), request.getBidderId())) {
            throw new IllegalArgumentException("Cannot bid on your own item.");
        }

        double formerBid = auctionItem.getCurrentBidAmount();
        if (formerBid >= request.getAmount()) {
            throw new IllegalArgumentException("Bid is not greater than current bid amount.");
        }
    }

    public void finalizeAuction(String auctionItemId) {
        AuctionItem auctionItem = auctionItemRepo.findById(auctionItemId)
                .orElseThrow(() -> new IllegalArgumentException("Auction item not found."));
        LocalDate auctionEndDate = auctionItem.getAuctionStartDate().plus(auctionItem.getDuration());
        validateAuctionTime(auctionEndDate, auctionItem);
        createTransaction(auctionItemId, auctionItem);
        auctionItemRepo.delete(auctionItem);
    }

    private void createTransaction(String auctionItemId, AuctionItem auctionItem) {
        Transaction transaction = new Transaction();
        transaction.setSellerId(auctionItem.getSellerId());
        transaction.setBuyerId(auctionItem.getCurrentBidderId());
        transaction.setAmount(auctionItem.getCurrentBidAmount());
        transaction.setAuctionItemId(auctionItemId);
        transactionRepo.save(transaction);
    }

    private static void validateAuctionTime(LocalDate auctionEndDate, AuctionItem auctionItem) {
        if (!LocalDate.now().isAfter(auctionEndDate)) {
            throw new IllegalArgumentException("Auction is still ongoing.");
        }

        String winnerId = auctionItem.getCurrentBidderId();
        if (winnerId == null) {
            throw new IllegalArgumentException("No bids were placed; auction cannot be finalized.");
        }
    }

    @Override
    public AuctionItem getAuctionItemById(String id) {
        return auctionItemRepo.findById(id).orElse(null);
    }

}
