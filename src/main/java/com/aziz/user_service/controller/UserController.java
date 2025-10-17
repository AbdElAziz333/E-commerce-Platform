package com.aziz.user_service.controller;

import com.aziz.user_service.dto.CurrentUserDto;
import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserRegisterRequest;
import com.aziz.user_service.dto.UserUpdateRequest;
import com.aziz.user_service.service.UserService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<CurrentUserDto>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully fetched the authenticated user",
                        service.getCurrentUser(userDetails)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.success("Fetched all users", service.getAllUsers())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(String.format("User with id %s, fetched successfully", id), service.getUserById(id)));
    }

    @GetMapping(params = "email")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.success(String.format("User with email %s, fetched successfully", email), service.getUserByEmail(email)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> addUser(@RequestBody UserRegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User Registered Successfully", service.addUser(registerRequest)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserUpdateRequest updateRequest) {
        return ResponseEntity.ok(ApiResponse.success(String.format("User with id %s, updated successfully", updateRequest.getId()), service.updateUser(updateRequest)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email
    ) {
        if (id != null) {
            service.deleteUserById(id);
            return ResponseEntity.ok(ApiResponse.success(String.format("User with id: %s deleted successfully", id), null));
        } else if (email != null) {
            service.deleteUserByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(String.format("User with email: %s deleted successfully", email), null));
        } else {
            throw new IllegalArgumentException("Either must be provided.");
        }
    }
}