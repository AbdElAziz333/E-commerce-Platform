package com.aziz.user_service.service;

import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.util.exceptions.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public String login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken(authentication);
            jwtService.addJwtToCookieResponse(jwt, response);

            return jwt;
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Email or password is invalid");
        }
    }
}