package com.aziz.api_gateway.client;

import com.aziz.api_gateway.dto.*;
import com.aziz.api_gateway.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @PostMapping("/api/v1/internal/registration")
    ApiResponse<String> createUser(@RequestBody RegistrationRequest request);

    @PostMapping("/api/v1/internal/registration/verify-otp")
    ApiResponse<PendingUserDto> verifyOtp(@RequestBody OtpVerificationRequest request);

    @PostMapping("/api/v1/internal/auth/verify")
    ApiResponse<UserDto> verifyCredentials(@RequestBody LoginRequest request);

    @GetMapping("/api/v1/internal/user/current")
    ApiResponse<CurrentUserDto> getCurrentUser();
}