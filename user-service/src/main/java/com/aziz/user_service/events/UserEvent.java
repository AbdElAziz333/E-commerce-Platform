package com.aziz.user_service.events;

import com.aziz.user_service.kafka.UserEventType;
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