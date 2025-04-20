package org.BidNest2.services.servicesImpl;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.models.Subscribe;
import org.BidNest2.data.models.User;
import org.BidNest2.data.repositories.AuctionItemRepo;
import org.BidNest2.data.repositories.SubscribeRepo;
import org.BidNest2.data.repositories.UserRepo;
import org.BidNest2.dtos.requests.AuctionItemRequest;
import org.BidNest2.dtos.requests.GetAuctionItemsRequest;
import org.BidNest2.dtos.requests.SubscribeRequest;
import org.BidNest2.dtos.requests.UnsubscribeRequest;
import org.BidNest2.services.servicesInterface.UserService;
import org.BidNest2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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



}
