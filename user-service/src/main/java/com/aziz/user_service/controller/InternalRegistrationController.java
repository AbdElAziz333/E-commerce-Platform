package com.aziz.user_service.controller;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.request.OtpVerificationRequest;
import com.aziz.user_service.request.RegistrationRequest;
import com.aziz.user_service.service.InternalRegistrationService;
import com.aziz.user_service.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/internal/registration")
@RequiredArgsConstructor
public class InternalRegistrationController {
    private final InternalRegistrationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> createUser(@RequestBody @Valid RegistrationRequest request) {
        return ApiResponse.success("User Registered Successfully", service.createUser(request));
    }

    @PostMapping("/verify-otp")
    public ApiResponse<AuthUserDto> verifyOtp(@RequestBody @Valid OtpVerificationRequest request) {
        return ApiResponse.success("Verified OTP Successfully", service.verifyOtp(request));
    }
}