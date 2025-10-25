package com.aziz.api_gateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
    @NotBlank(message = "verification id must not be blank")
    private String verificationId;

    @NotBlank(message = "otp must not be blank")
    private String otp;
}