package com.aziz.notification_service.model;

import com.aziz.notification_service.util.enums.NotificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Table
public class Notification {
    @PrimaryKey
    @Column("notification_id")
    private UUID notificationId;

    private String email;
    private String title;
    private String message;
    private NotificationStatus status;

    @Column("sent_at")
    private LocalDateTime sentAt;

    @Column("read_at")
    private LocalDateTime readAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("error_message")
    private String errorMessage;
}