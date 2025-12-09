package com.aziz.auth_service.controller;

import com.aziz.auth_service.dto.OtpVerificationRequest;
import com.aziz.auth_service.dto.RegistrationRequest;
import com.aziz.auth_service.service.RegistrationService;
import com.aziz.auth_service.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPendingUser(@RequestBody RegistrationRequest request) {
        String verificationId = registrationService.signup(request);

        return ResponseEntity.ok(ApiResponse.success(
                "Successfully registered, please check your email and verify the otp", verificationId));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@RequestBody OtpVerificationRequest request, HttpServletResponse response) {
        registrationService.verifyOtp(request, response);
        return ResponseEntity.ok(ApiResponse.success("Successfully verified, logging in...", null));
    }
}