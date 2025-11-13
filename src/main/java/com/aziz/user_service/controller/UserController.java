package com.aziz.user_service.controller;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserUpdateRequest;
import com.aziz.user_service.service.UserService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all users", userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userService.getUserById(id)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.updateUser(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}