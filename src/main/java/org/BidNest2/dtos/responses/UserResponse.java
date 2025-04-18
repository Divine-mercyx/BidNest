package org.BidNest2.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.BidNest2.data.models.Profile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String token;
    String id;
    String email;
    Profile profile;
}
