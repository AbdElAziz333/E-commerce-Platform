package com.aziz.user_service.controller;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserUpdateRequest;
import com.aziz.user_service.service.UserService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getUsersPage(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(ApiResponse.success("Fetched users for page " + page, userService.getUsersPage(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userService.getUserById(id)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestHeader("User-Id") Long userId, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.updateUser(userId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}