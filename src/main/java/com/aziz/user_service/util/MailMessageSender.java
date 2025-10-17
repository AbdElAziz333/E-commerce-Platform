package com.aziz.user_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailMessageSender {
    private final JavaMailSender mailSender;

    /**
     * Sends a verification message to the email registered.
     * @param to the email to the registered user
     * @param otp the one time password sent to da email
     * @author Aziz
     * */
    public void sendEmailVerification(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to E-commerce");
        message.setText(String.format("Halo, your OTP: %s\nIt will expire after some time, please don't share it with anyone.", otp));
        mailSender.send(message);
    }

    /**
     * Sends a greetings message to the user that verified its OTP.
     * @param email the email to the registered user
     * @param firstName the firstName of the user
     * @author Aziz
     * */
    public void sendEmailVerified(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Thanks for using our services!");
        message.setText(String.format("Halo %s, thanks for verifying the OTP and using our services! <3", firstName));
        mailSender.send(message);
    }
}