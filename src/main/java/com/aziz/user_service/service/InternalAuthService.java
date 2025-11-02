package com.aziz.user_service.service;

import com.aziz.user_service.dto.AuthUserDto;
import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.exceptions.BadCredentialsException;
import com.aziz.user_service.util.exceptions.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InternalAuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthUserDto verifyCredentials(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new AuthUserDto(user.getUserId(), user.getRole());
    }
}