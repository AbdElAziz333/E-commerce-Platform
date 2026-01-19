package com.aziz.user_service.controller;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.request.UserUpdateRequest;
import com.aziz.user_service.service.UserService;
import com.aziz.user_service.util.ApiResponse;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.ok(ApiResponse.success("Fetched users for page " + page, userService.getUsers(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userService.getUserById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}