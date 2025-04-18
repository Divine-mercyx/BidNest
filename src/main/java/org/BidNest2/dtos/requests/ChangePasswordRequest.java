package org.BidNest2.dtos.requests;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    String email;
    String newPassword;
}
