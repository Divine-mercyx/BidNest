package org.BidNest2.services.servicesInterface;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
}
