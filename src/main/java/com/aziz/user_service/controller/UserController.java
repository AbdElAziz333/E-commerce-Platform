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
    private final UserService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all users", service.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(String.format("User with id %s, fetched successfully", id), service.getUserById(id)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserUpdateRequest updateRequest) {
        return ResponseEntity.ok(ApiResponse.success(String.format("User with id %s, updated successfully", updateRequest.getId()), service.updateUser(updateRequest)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.success(String.format("User with id: %s deleted successfully", id), null));
    }
}