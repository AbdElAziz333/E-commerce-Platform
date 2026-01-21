package com.aziz.auth_service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {
    @NotBlank
    private String verificationId;

    @NotBlank
    private String otp;

    @Email
    @NotBlank
    private String email;
}