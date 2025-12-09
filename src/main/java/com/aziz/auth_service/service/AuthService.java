package com.aziz.auth_service.service;

import com.aziz.auth_service.client.UserFeignClient;
import com.aziz.auth_service.config.JwtConfig;
import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.dto.CurrentUserDto;
import com.aziz.auth_service.dto.LoginRequest;
import com.aziz.auth_service.repository.RefreshTokenRepository;
import com.aziz.auth_service.util.ApiResponse;
import com.aziz.auth_service.util.TokenEncryptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserFeignClient userFeignClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;
    private final TokenEncryptor encryptor;

    public Map<String, String> login(LoginRequest loginRequest, HttpServletResponse httpResponse) {
        log.debug("Attempting to login user with email: {}", loginRequest.getEmail());
        ApiResponse<AuthUserDto> response = userFeignClient.verifyCredentials(loginRequest);
        AuthUserDto user = response.getData();

        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUserId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(user.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        log.info("User successfully logged in with email: {}", loginRequest.getEmail());

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

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        Optional<String> storedToken = refreshTokenRepository.findRefreshToken(tokenId);

        if (storedToken.isEmpty() || !encryptor.compare(refreshToken, storedToken.get())) {
            throw new RuntimeException("Refresh token not found or has been revoked");
        }

        Long userId = jwtService.getUserIdFromJwt(refreshToken, true);

        ApiResponse<CurrentUserDto> userResponse = userFeignClient.getCurrentUser(userId);
        CurrentUserDto dto = userResponse.getData();

        if (dto == null) {
            throw new RuntimeException("User not found");
        }

        refreshTokenRepository.delete(tokenId);

        String newAccessToken = jwtService.generateAccessToken(dto.getUserId(), dto.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(dto.getUserId());
        String newTokenId = jwtService.getJtiFromJwt(newRefreshToken);

        refreshTokenRepository.saveToken(userId, newTokenId, newRefreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        jwtService.addAccessTokenToCookie(newAccessToken, response);
        jwtService.addRefreshTokenToCookie(newRefreshToken, response);

        return "Token refreshed";
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.parseRefreshToken(request);

        if (refreshToken != null) {
            String tokenId = jwtService.getJtiFromJwt(refreshToken);
            refreshTokenRepository.delete(tokenId);
        }

        jwtService.clearAccessTokenCookie(response);
        jwtService.clearRefreshTokenCookie(response);
    }
}