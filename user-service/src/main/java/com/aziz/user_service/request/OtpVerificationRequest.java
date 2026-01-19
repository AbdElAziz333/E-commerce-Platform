package com.aziz.user_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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