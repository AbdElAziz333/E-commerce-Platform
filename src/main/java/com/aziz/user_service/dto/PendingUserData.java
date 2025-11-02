package com.aziz.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserData {
    private String verificationId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}