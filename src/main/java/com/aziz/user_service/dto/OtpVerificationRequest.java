package com.aziz.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpVerificationRequest {
    private String verificationId;
    private String email;
    private String otp;
}