package com.aziz.user_service.kafka;

import com.aziz.user_service.config.KafkaConfig;
import com.aziz.user_service.kafka.events.types.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPublisher {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final KafkaConfig config;

    public void publishOtp(String email, String otp) {
        UserEvent event = new UserEvent(UserEventType.USER_OTP_VERIFICATION, email, otp, null);
        kafkaTemplate.send(config.getUserEvents(), event.getEmail(), event);
    }

    public void publishWelcome(String email, String firstName) {
        UserEvent event = new UserEvent(UserEventType.USER_REGISTERED, email, null, firstName);
        kafkaTemplate.send(config.getUserEvents(), event.getEmail(), event);
    }
}