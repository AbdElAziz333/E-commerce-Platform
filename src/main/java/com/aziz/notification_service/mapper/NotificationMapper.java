package com.aziz.notification_service.mapper;

import com.aziz.notification_service.dto.NotificationDto;
import com.aziz.notification_service.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    public NotificationDto notificationToDto(Notification notification) {
        return NotificationDto.builder()
                .email(notification.getEmail())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .build();
    }
}