package com.aziz.notification_service.mail;

import com.aziz.notification_service.dto.NotificationDto;
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
     * @param notification the notification object
     * @author Aziz
     * */
    public void send(NotificationDto notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getEmail());
        message.setSubject(notification.getTitle());
        message.setText(notification.getMessage());

        mailSender.send(message);
    }
}