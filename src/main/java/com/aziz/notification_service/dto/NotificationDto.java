package com.aziz.notification_service.dto;

import com.aziz.notification_service.util.enums.NotificationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String email;
    private String title;
    private String message;
    private NotificationStatus status;
}