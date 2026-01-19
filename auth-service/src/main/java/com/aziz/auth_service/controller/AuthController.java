package com.aziz.auth_service.controller;

import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.request.LoginRequest;
import com.aziz.auth_service.service.AuthService;
import com.aziz.auth_service.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthUserDto>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        AuthUserDto userDto = service.login(request, response);
        return ResponseEntity.ok(ApiResponse.success("Successfully verified credentials", userDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        service.refreshToken(request, response);
        return ResponseEntity.ok(ApiResponse.success("Successfully refreshed token", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok(ApiResponse.success("Successfully logout", null));
    }
}