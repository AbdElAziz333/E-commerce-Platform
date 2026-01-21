package com.aziz.auth_service.service;

import com.aziz.auth_service.config.JwtConfig;
import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.model.User;
import com.aziz.auth_service.repository.RefreshTokenRepository;
import com.aziz.auth_service.repository.UserRepository;
import com.aziz.auth_service.request.LoginRequest;
import com.aziz.auth_service.util.TokenEncryptor;
import com.aziz.auth_service.util.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;
    private final TokenEncryptor encryptor;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthUserDto login(LoginRequest request, HttpServletResponse httpResponse) {
        log.debug("Attempting to login user with email: {}", request.getEmail());

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot verify credentials for user with email: {}, user not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password are wrong, please enter a valid one");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(user.getId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        log.info("User successfully logged in with email: {}", request.getEmail());

        return new AuthUserDto(user.getId(), user.getRole());
    }

    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.parseRefreshToken(request);

        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token is invalid or expired.");
        }

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        Optional<String> storedToken = refreshTokenRepository.findRefreshToken(tokenId);

        if (storedToken.isEmpty() || !encryptor.compare(refreshToken, storedToken.get())) {
            throw new UnauthorizedException("Refresh token not found or has been revoked");
        }

        Long userId = jwtService.getUserIdFromJwt(refreshToken, true);

        User user = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId));

        AuthUserDto dto = new AuthUserDto(user.getId(), user.getRole());

        refreshTokenRepository.delete(tokenId);

        String newAccessToken = jwtService.generateAccessToken(dto.getUserId(), dto.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(dto.getUserId());
        String newTokenId = jwtService.getJtiFromJwt(newRefreshToken);

        refreshTokenRepository.saveToken(userId, newTokenId, newRefreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));

        jwtService.addAccessTokenToCookie(newAccessToken, response);
        jwtService.addRefreshTokenToCookie(newRefreshToken, response);
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