package com.aziz.user_service.controller;

import com.aziz.user_service.dto.CurrentUserDto;
import com.aziz.user_service.service.InternalUserService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/user")
@RequiredArgsConstructor
public class InternalUserController {
    private final InternalUserService service;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<CurrentUserDto>> getCurrentUser(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully fetched the authenticated user",
                        service.getCurrentUser(userId))
        );
    }
}