package org.BidNest2.dtos.requests;

import lombok.Data;

@Data
public class SubscribeRequest {
    private String subscriberId;
    private String subscriberToId;
    private String token;
}
