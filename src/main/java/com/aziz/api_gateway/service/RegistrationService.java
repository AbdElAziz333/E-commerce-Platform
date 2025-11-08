package com.aziz.api_gateway.service;

import com.aziz.api_gateway.client.UserFeignClient;
import com.aziz.api_gateway.config.JwtConfig;
import com.aziz.api_gateway.dto.AuthUserDto;
import com.aziz.api_gateway.dto.OtpVerificationRequest;
import com.aziz.api_gateway.dto.RegistrationRequest;
import com.aziz.api_gateway.repository.RefreshTokenRepository;
import com.aziz.api_gateway.util.Role;
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
        String accessToken = jwtService.generateAccessToken(dto.getUserId(), Role.ROLE_USER.name());
        String refreshToken = jwtService.generateRefreshToken(dto.getUserId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(dto.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));
    }
}