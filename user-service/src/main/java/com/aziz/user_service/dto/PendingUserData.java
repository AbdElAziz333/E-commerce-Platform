package com.aziz.user_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PendingUserData {
    private String verificationId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}