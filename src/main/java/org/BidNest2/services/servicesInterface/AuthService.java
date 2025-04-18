package org.BidNest2.services.servicesInterface;

import org.BidNest2.data.models.User;
import org.BidNest2.dtos.requests.ChangePasswordRequest;
import org.BidNest2.dtos.requests.UserLoginRequest;
import org.BidNest2.dtos.responses.UserResponse;

public interface AuthService {
    UserResponse login(UserLoginRequest loginRequest);
    UserResponse register(User user);
    String sendOtp(String email);
    void resetPassword(ChangePasswordRequest changePasswordRequest);
    void deleteAccount(String id);
    void deleteAll();
    Long count();
}
