package com.aziz.user_service.controller;

import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/internal/auth")
@RestController
@RequiredArgsConstructor
public class InternalAuthController {
    private final AuthenticationManager authManager;
    private final UserRepository repository;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<UserDto>> verifyCredentials(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDto dto = new UserDto(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getRole());
            return ResponseEntity.ok(ApiResponse.success("Successfully verified credentials", dto));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
