package com.aziz.auth_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "REGISTRATION_SESSION", timeToLive = 600) // 600 seconds 10 minutes
public class RegistrationSession {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password; //hashed
    private String phoneNumber;

    private String otp;
}