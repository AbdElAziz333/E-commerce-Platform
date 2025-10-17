package com.aziz.user_service.controller;

import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.service.AuthService;
import com.aziz.user_service.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success("Successfully logged in", service.login(request, response)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .sameSite("Lax")
                .maxAge(0)
                .path("/")
                .secure(false)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(ApiResponse.success("Successfully logout", null));
    }
}