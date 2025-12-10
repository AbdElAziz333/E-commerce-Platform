package com.aziz.notification_service.kafka.events;

import com.aziz.notification_service.kafka.events.types.UserEventType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private UserEventType type;
    private String email;
    private String otp;
    private String firstName;
}