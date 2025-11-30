package com.aziz.auth_service.client;

import com.aziz.auth_service.dto.*;
import com.aziz.auth_service.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @PostMapping("/api/v1/internal/registration")
    ApiResponse<String> createUser(@RequestBody RegistrationRequest request);

    @PostMapping("/api/v1/internal/registration/verify-otp")
    ApiResponse<AuthUserDto> verifyOtp(@RequestBody OtpVerificationRequest request);

    @PostMapping("/api/v1/internal/auth/verify")
    ApiResponse<AuthUserDto> verifyCredentials(@RequestBody LoginRequest request);

    @GetMapping("/api/v1/internal/auth/current")
    ApiResponse<CurrentUserDto> getCurrentUser(@RequestHeader("User-Id") Long userId);
}