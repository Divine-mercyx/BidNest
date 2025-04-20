package org.BidNest2.controllers;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.models.Subscribe;
import org.BidNest2.dtos.requests.*;
import org.BidNest2.services.servicesInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorized")
public class UserServiceController {

    @Autowired
    private UserService userServiceImpl;

    @PostMapping("/auction")
    public ResponseEntity<AuctionItem> auctionItem(@RequestBody AuctionItemRequest request) {
        return ResponseEntity.ok(userServiceImpl.auctionItem(request));
    }

    @GetMapping("/auctionItems")
    public ResponseEntity<List<AuctionItem>> getAuctionedItems(@RequestBody GetAuctionItemsRequest request) {
        return ResponseEntity.ok(userServiceImpl.getAllAuctionItems(request));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Subscribe> subscribe(@RequestBody SubscribeRequest request) {
        return ResponseEntity.ok(userServiceImpl.subscribe(request));
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody UnsubscribeRequest request) {
        userServiceImpl.unSubscribe(request);
        return ResponseEntity.ok("Unsubscribed");
    }

    @PostMapping("/bid")
    public ResponseEntity<String> bid(@RequestBody BIdRequest request) {
        userServiceImpl.bid(request);
        return ResponseEntity.ok("you have successfully bid for this item");
    }
}
