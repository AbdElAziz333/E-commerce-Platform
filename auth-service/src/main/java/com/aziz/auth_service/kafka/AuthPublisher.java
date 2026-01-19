package com.aziz.auth_service.kafka;

import com.aziz.auth_service.config.KafkaConfig;
import com.aziz.auth_service.kafka.events.OtpVerificationEvent;
import com.aziz.auth_service.kafka.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig config;

    /**
     * Publish OTP Verification event to notification-service
     * */
    public void publishOtp(String email, String otp) {
        OtpVerificationEvent event = new OtpVerificationEvent(email, otp);
        kafkaTemplate.send(config.getOtpVerification(), event.getEmail(), event);
    }

    /**
     * Publish user registered event to notification-service
     * */
    public void publishWelcome(String email, String firstName) {
        UserRegisteredEvent event = new UserRegisteredEvent(email, firstName);
        kafkaTemplate.send(config.getUserRegistered(), event.getEmail(), event);
    }
}