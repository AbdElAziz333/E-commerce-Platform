package com.aziz.user_service.controller;

import com.aziz.user_service.dto.AuthUserDto;
import com.aziz.user_service.dto.CurrentUserDto;
import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.service.InternalAuthService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/auth")
@RequiredArgsConstructor
public class InternalAuthController {
    private final InternalAuthService service;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<AuthUserDto>> verifyCredentials(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Successfully verified credentials", service.verifyCredentials(request)));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<CurrentUserDto>> getCurrentUser(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched user", service.getCurrentUser(userId)));
    }

    @GetMapping("/current/email")
    public ResponseEntity<ApiResponse<String>> getCurrentUserEmail(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched current user's email", service.getCurrentUserEmail(userId)));
    }
}