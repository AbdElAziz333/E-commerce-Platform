package com.aziz.user_service.controller;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.service.InternalRegistrationService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/registration")
@RequiredArgsConstructor
public class InternalRegistrationController {
    private final InternalRegistrationService service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody RegistrationRequest registerRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User Registered Successfully", service.createUser(registerRequest)));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthUserDto>> verifyOtp(@RequestBody OtpVerificationRequest request) {
        return ResponseEntity
                .ok(
                        ApiResponse.success("Verified OTP Successfully",
                                service.verifyOtp(request)));
    }
}