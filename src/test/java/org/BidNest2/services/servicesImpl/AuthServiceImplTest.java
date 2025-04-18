package org.BidNest2.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.BidNest2.data.models.User;
import org.BidNest2.dtos.requests.UserLoginRequest;
import org.BidNest2.dtos.responses.UserResponse;
import org.BidNest2.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authService;

    private User user;


    @BeforeEach
    void setUp() {
        authService.deleteAll();
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
    }

    @AfterEach
    void tearDown() {
        authService.deleteAll();
    }

    @Test
    public void loginUser_ThrowUserNotFoundException_authServicesTest() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        assertThrows(UserNotFoundException.class, () -> authService.login(request));
    }

    @Test
    public void saveUser_loginUser_authServicesTest() {
        UserResponse registeredUser = authService.register(user);
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getToken());
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        UserResponse loggedInUser = authService.login(request);
        assertNotNull(loggedInUser);
        assertNotNull(loggedInUser.getToken());
        assertEquals(registeredUser.getEmail(), loggedInUser.getEmail());
    }

    @Test
    public void saveUser_deleteUser_authServicesTest() {
        UserResponse registeredUser = authService.register(user);
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getToken());
        log.info(registeredUser.getToken());
        assertEquals(authService.count(), 1);
        authService.deleteAccount(registeredUser.getId());
        assertEquals(authService.count(), 0);
    }
}