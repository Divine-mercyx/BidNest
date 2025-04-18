package org.BidNest2.services.servicesImpl;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.repositories.AuctionItemRepo;
import org.BidNest2.dtos.requests.AuctionItemRequest;
import org.BidNest2.dtos.requests.GetAuctionItemsRequest;
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

    @Override
    public AuctionItem auctionItem(AuctionItemRequest request) {
        if (!jwtUtils.validateToken(request.getToken(), request.getId())) throw new IllegalArgumentException("Invalid token");
        if (request.getToken() == null || request.getToken().isEmpty()) throw new IllegalArgumentException("token is empty");
        return auctionItemRepo.save(request.getAuctionItem());
    }

    @Override
    public void deleteAll() {
        auctionItemRepo.deleteAll();
    }

    @Override
    public List<AuctionItem> getAllAuctionItems(GetAuctionItemsRequest request) {
        if (!jwtUtils.validateToken(request.getToken(), request.getId())) throw new IllegalArgumentException("Invalid token");
        return auctionItemRepo.findBySellerId(request.getId());
    }

}
