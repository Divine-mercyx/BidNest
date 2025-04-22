package org.BidNest2.controllers;

import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.dtos.requests.BidRequest;
import org.BidNest2.services.servicesInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/bid")
    public void handleBid(BidRequest request) {
        userService.bid(request);
        AuctionItem auctionItem = userService.getAuctionItemById(request.getItemId());
        messagingTemplate.convertAndSend("/topic/auction/" + request.getItemId(), auctionItem);
    }
}
