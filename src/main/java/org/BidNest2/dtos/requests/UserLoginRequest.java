package org.BidNest2.dtos.requests;

import lombok.Data;

@Data
public class UserLoginRequest {
    String email;
    String password;
}
