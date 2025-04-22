package org.BidNest2.config;

import lombok.extern.slf4j.Slf4j;
import org.BidNest2.data.models.User;
import org.BidNest2.services.servicesInterface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@Slf4j
public class AdminConfig implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmail("admin@admin.com");
        user.setPassword("admin123");
        authService.register(user);
        log.info("Admin registered");
    }
}
