package org.BidNest2.services.servicesImpl;

import org.BidNest2.data.models.Otp;
import org.BidNest2.data.repositories.OtpRepository;
import org.BidNest2.services.servicesInterface.EmailService;
import org.BidNest2.services.servicesInterface.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private EmailService emailServiceImpl;

    @Autowired
    private OtpRepository otpRepository;

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String sendOtp(String email) {
        int otp = 1000 + random.nextInt(9000);
        Otp otpObj = new Otp();
        otpObj.setCode(String.valueOf(otp));
        otpRepository.save(otpObj);
        emailServiceImpl.sendSimpleMail(email, "OTP Code", otpObj.getCode());
        return otpObj.getCode();
    }
}
