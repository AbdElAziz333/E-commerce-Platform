package com.aziz.notification_service.service;

import com.aziz.notification_service.mail.MailMessageSender;
import com.aziz.notification_service.mapper.NotificationMapper;
import com.aziz.notification_service.model.Notification;
import com.aziz.notification_service.util.enums.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MailMessageSender mailSender;
    private final NotificationMapper mapper;

    public void sendNotification(String email, String title, String message) {
        Notification notification = Notification.builder()
                .notificationId(UUID.randomUUID())
                .email(email)
                .title(title)
                .message(message)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            mailSender.send(mapper.notificationToDto(notification));
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            log.info("Email sent successfully to: {}", email);
        } catch (MailException e) {
            log.error("Email sending failed to: {}: {}", email, e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        }

        log.info("Notification saved with status: {} for email: {}", notification.getStatus(), notification.getEmail());
    }
}