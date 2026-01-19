package com.aziz.auth_service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationRequest {
    @NotBlank
    private String verificationId;

    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}