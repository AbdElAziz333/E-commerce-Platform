package com.aziz.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpVerificationRequest {
    private String verificationId;
    private String email;
    private String otp;
}