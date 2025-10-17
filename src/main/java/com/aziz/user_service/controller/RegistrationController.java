package com.aziz.user_service.controller;

import com.aziz.user_service.dto.OtpRequest;
import com.aziz.user_service.dto.RegistrationRequest;
import com.aziz.user_service.service.RegistrationService;
import com.aziz.user_service.util.ApiResponse;
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
    private final RegistrationService service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPendingUser(@RequestBody RegistrationRequest request) {
        String verificationId = service.signup(request);

        return ResponseEntity.ok(ApiResponse.success(
                "Successfully registered, please check your email and verify the otp", verificationId));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtpAndActiveUser(@RequestBody OtpRequest request, HttpServletResponse httpResponse) {
        service.verifyOtp(request, httpResponse);

        return ResponseEntity.ok(ApiResponse.success("Successfully verified, logging in...", null));
    }
}