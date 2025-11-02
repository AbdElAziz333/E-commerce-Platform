package com.aziz.api_gateway.controller;

import com.aziz.api_gateway.dto.LoginRequest;
import com.aziz.api_gateway.service.AuthService;
import com.aziz.api_gateway.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    // it returns the jwt
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success("Successfully logged in", service.login(request, response)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        service.logout(response);
        return ResponseEntity.ok(ApiResponse.success("Successfully logout", null));
    }
}