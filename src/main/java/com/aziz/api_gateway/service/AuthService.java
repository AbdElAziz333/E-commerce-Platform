package com.aziz.api_gateway.service;

import com.aziz.api_gateway.client.UserFeignClient;
import com.aziz.api_gateway.dto.LoginRequest;
import com.aziz.api_gateway.dto.UserDto;
import com.aziz.api_gateway.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtService jwtService;
    private final UserFeignClient authClient;

    public String login(LoginRequest loginRequest, HttpServletResponse httpResponse) {
        // here, if successfully verified credentials it will return user data
        // if not it will through exception
        ApiResponse<UserDto> response = authClient.verifyCredentials(loginRequest);
        UserDto user = response.getData();
        String jwt = jwtService.generateToken(user.getEmail());
        jwtService.addJwtToCookieResponse(jwt, httpResponse);

        return jwt;
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .sameSite("Lax")
                .maxAge(0)
                .path("/")
                .secure(false)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}