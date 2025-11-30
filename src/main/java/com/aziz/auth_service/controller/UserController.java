package com.aziz.auth_service.controller;

import com.aziz.auth_service.client.UserFeignClient;
import com.aziz.auth_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserFeignClient userFeignClient;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("User-Id") Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User-Id header is missing"));
        }

        return ResponseEntity.ok(userFeignClient.getCurrentUser(userId));
    }
}