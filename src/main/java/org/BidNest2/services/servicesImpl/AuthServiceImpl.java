package org.BidNest2.services.servicesImpl;

import org.BidNest2.data.models.User;
import org.BidNest2.data.repositories.UserRepo;
import org.BidNest2.dtos.requests.ChangePasswordRequest;
import org.BidNest2.dtos.requests.UserLoginRequest;
import org.BidNest2.dtos.responses.UserResponse;
import org.BidNest2.exceptions.DuplicateEmailException;
import org.BidNest2.exceptions.IncorrectPasswordException;
import org.BidNest2.exceptions.UserNotFoundException;
import org.BidNest2.services.servicesInterface.AuthService;
import org.BidNest2.services.servicesInterface.OtpService;
import org.BidNest2.utils.JwtUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OtpService otpServiceImpl;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public UserResponse login(UserLoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail());
        validateUser(loginRequest, user);
        String token = jwtUtils.generateToken(user.getId());
        return new UserResponse(token, user.getId(), user.getEmail(), user.getProfile());
    }

    private static void validateUser(UserLoginRequest loginRequest, User user) {
        if (user == null) throw new UserNotFoundException("User not found");
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) throw new IncorrectPasswordException("Incorrect email or password");
    }

    @Override
    public UserResponse register(User user) {
        User savedUser = validateUserRegistration(user);
        String token = jwtUtils.generateToken(savedUser.getId());
        return new UserResponse(token, savedUser.getId(), user.getEmail(), user.getProfile());
    }

    private User validateUserRegistration(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) throw new DuplicateEmailException("Email already exists");
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) throw new UserNotFoundException("Email or password is empty");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepo.save(user);
    }

    @Override
    public String sendOtp(String email) {
        return otpServiceImpl.sendOtp(email);
    }

    @Override
    public void resetPassword(ChangePasswordRequest request) {
        if (isValidEmail(request.getEmail())) throw new NullPointerException("Invalid email");
        if (request.getNewPassword().isEmpty()) throw new NullPointerException("New password is empty");
        User user = userRepo.findByEmail(request.getEmail());
        if (user == null) throw new NullPointerException("User not found");
        User hashedUser = hashPassword(user);
        user.setPassword(hashedUser.getPassword());
        userRepo.save(user);
    }

    @Override
    public void deleteAccount(String id) {
        userRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepo.deleteAll();
    }

    @Override
    public Long count() {
        return userRepo.count();
    }

    private static User hashPassword(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        return user;
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}
