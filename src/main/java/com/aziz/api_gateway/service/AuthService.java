package com.aziz.api_gateway.service;

import com.aziz.api_gateway.client.UserFeignClient;
import com.aziz.api_gateway.config.JwtConfig;
import com.aziz.api_gateway.dto.AuthUserDto;
import com.aziz.api_gateway.dto.LoginRequest;
import com.aziz.api_gateway.repository.RefreshTokenRepository;
import com.aziz.api_gateway.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserFeignClient userFeignClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    public Map<String, String> login(LoginRequest loginRequest, HttpServletResponse httpResponse) {
        ApiResponse<AuthUserDto> response = userFeignClient.verifyCredentials(loginRequest);
        AuthUserDto user = response.getData();

        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUserId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(user.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        return Map.of(
                "userId", user.getUserId().toString(),
                "role", user.getRole().name()
        );
    }

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.parseRefreshToken(request);

        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token is invalid or expired.");
        }

        Long userId = jwtService.getUserIdFromJwt(refreshToken, true);

        @SuppressWarnings("unchecked")
        ResponseEntity<ApiResponse<AuthUserDto>> userResponse = (ResponseEntity<ApiResponse<AuthUserDto>>) userFeignClient.getCurrentUser(userId);
        AuthUserDto user = userResponse.getBody().getData();

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String newAccessToken = jwtService.generateAccessToken(user.getUserId(), user.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUserId());

        String tokenId = jwtService.getJtiFromJwt(newRefreshToken);

        refreshTokenRepository.saveToken(userId, tokenId, newRefreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        jwtService.addAccessTokenToCookie(newAccessToken, response);
        jwtService.addRefreshTokenToCookie(newRefreshToken, response);

        return "Token refreshed";
    }

    public void logout(HttpServletResponse response) {
        jwtService.clearAccessTokenCookie(response);
        jwtService.clearRefreshTokenCookie(response);
    }
}