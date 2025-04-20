package org.BidNest2.controllers;

import org.BidNest2.data.models.User;
import org.BidNest2.dtos.requests.ChangePasswordRequest;
import org.BidNest2.dtos.requests.UserLoginRequest;
import org.BidNest2.dtos.responses.UserResponse;
import org.BidNest2.services.servicesInterface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/sendOtp/{email}")
    public ResponseEntity<String> sendOtp(@PathVariable String email) {
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.resetPassword(changePasswordRequest);
        return ResponseEntity.ok("Password reset sent");
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable String id) {
        authService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted");
    }
}
