package com.aziz.auth_service.controller;

import com.aziz.auth_service.request.VerifyOtpRequest;
import com.aziz.auth_service.request.RegistrationRequest;
import com.aziz.auth_service.service.RegistrationService;
import com.aziz.auth_service.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody @Valid RegistrationRequest request
    ) {
        String verificationId = service.signup(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                "Registration successful. Please check your email for the OTP.",
                        verificationId
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(
            @RequestBody @Valid VerifyOtpRequest request,
            HttpServletResponse response
    ) {
        service.verifyOtp(request, response);
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully. You can login.", null));
    }
}