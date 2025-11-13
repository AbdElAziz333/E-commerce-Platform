package com.aziz.auth_service.service;

import com.aziz.auth_service.client.UserFeignClient;
import com.aziz.auth_service.config.JwtConfig;
import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.dto.OtpVerificationRequest;
import com.aziz.auth_service.dto.RegistrationRequest;
import com.aziz.auth_service.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final JwtService jwtService;
    private final UserFeignClient feignClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    public String signup(RegistrationRequest request) {
        return feignClient.createUser(request).getData();
    }

    public void verifyOtp(OtpVerificationRequest request, HttpServletResponse httpResponse) {
        AuthUserDto dto = feignClient.verifyOtp(request).getData();

        String accessToken = jwtService.generateAccessToken(dto.getUserId(), dto.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(dto.getUserId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(dto.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));
    }
}