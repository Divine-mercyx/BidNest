package org.BidNest2.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.BidNest2.data.models.AuctionItem;
import org.BidNest2.data.models.User;
import org.BidNest2.dtos.requests.AuctionItemRequest;
import org.BidNest2.dtos.requests.GetAuctionItemsRequest;
import org.BidNest2.dtos.responses.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthServiceImpl authService;

    private AuctionItem auctionItem;
    private AuctionItem auctionItem1;
    private User user;

    @BeforeEach
    void setUp() {
        userService.deleteAll();
        authService.deleteAll();
        auctionItem = new AuctionItem();
        auctionItem.setAuctionStartDate(LocalDate.of(2025, 5, 5));
        auctionItem.setDuration(Duration.ofMinutes(10));
        auctionItem.setTitle("title");
        auctionItem.setDescription("description");
        auctionItem.setImageUrl("imageUrl");
        auctionItem.setCurrentBidAmount(40000.0);
        user = new User();
        user.setEmail("email@gmail.com");
        user.setPassword("password");

        auctionItem1 = new AuctionItem();
        auctionItem1.setAuctionStartDate(LocalDate.of(2025, 5, 5));
        auctionItem1.setDuration(Duration.ofMinutes(10));
        auctionItem1.setTitle("title2");
        auctionItem1.setDescription("description");
        auctionItem1.setImageUrl("imageUrl");
        auctionItem1.setCurrentBidAmount(40000.0);
    }

    @AfterEach
    void tearDown() {
        userService.deleteAll();
        authService.deleteAll();
    }

    @Test
    public void saveUser_createAuction_testUserServices() {
        UserResponse savedUser = authService.register(user);
        auctionItem.setSellerId(savedUser.getId());

        AuctionItemRequest request = new AuctionItemRequest();
        request.setId(savedUser.getId());
        request.setToken(savedUser.getToken());
        request.setAuctionItem(auctionItem);
        AuctionItem savedAuctionItem = userService.auctionItem(request);
        assertNotNull(savedAuctionItem);
        assertEquals(savedAuctionItem.getSellerId(), savedUser.getId());
    }

    @Test
    public void saveUser_createTwoAuctionItem_getAllAuctionedItems_testUserServices() {
        UserResponse savedUser = authService.register(user);
        auctionItem.setSellerId(savedUser.getId());

        AuctionItemRequest request = new AuctionItemRequest();
        request.setId(savedUser.getId());
        request.setToken(savedUser.getToken());
        request.setAuctionItem(auctionItem);
        AuctionItem savedAuctionItem = userService.auctionItem(request);


        auctionItem1.setSellerId(savedUser.getId());
        AuctionItemRequest request1 = new AuctionItemRequest();
        request1.setId(savedUser.getId());
        request1.setToken(savedUser.getToken());
        request1.setAuctionItem(auctionItem1);

        AuctionItem savedAuctionItem2 = userService.auctionItem(request1);
        assertNotNull(savedAuctionItem2);
        assertEquals(savedAuctionItem.getSellerId(), savedAuctionItem2.getSellerId());
        GetAuctionItemsRequest request2 = new GetAuctionItemsRequest();
        request2.setId(savedUser.getId());
        request2.setToken(savedUser.getToken());
        List<AuctionItem> allAuctionItems = userService.getAllAuctionItems(request2);
        assertNotNull(allAuctionItems);
        assertEquals(allAuctionItems.size(), 2);
    }
}