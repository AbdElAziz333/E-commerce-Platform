package com.aziz.notification_service.model;

import com.aziz.notification_service.util.enums.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private UUID notificationId;

    private String email;
    private String title;
    private String message;
    private NotificationStatus status;

    private LocalDateTime sentAt;

    private LocalDateTime createdAt;

    private String errorMessage;
}