package com.aziz.auth_service.controller;

import com.aziz.auth_service.dto.UserDto;
import com.aziz.auth_service.request.UserUpdateRequest;
import com.aziz.auth_service.service.UserService;
import com.aziz.auth_service.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(service.getCurrentUser(userId));
    }

    @GetMapping("/current/email")
    public ResponseEntity<ApiResponse<String>> getCurrentUserEmail(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched current user's email", service.getCurrentUserEmail(userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.ok(ApiResponse.success("Fetched users for page " + page, service.getUsers(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", service.getUserById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", service.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}