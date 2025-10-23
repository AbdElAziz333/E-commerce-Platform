package com.aziz.user_service.mapper;

import com.aziz.user_service.dto.RegistrationRequest;
import com.aziz.user_service.model.PendingUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingUserMapper {
    private final PasswordEncoder encoder;

    public PendingUser registerRequestToPendingUser(RegistrationRequest request) {
        return PendingUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}