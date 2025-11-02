package com.aziz.api_gateway.service;

import com.aziz.api_gateway.client.UserFeignClient;
import com.aziz.api_gateway.dto.AuthUserDto;
import com.aziz.api_gateway.dto.OtpVerificationRequest;
import com.aziz.api_gateway.dto.RegistrationRequest;
import com.aziz.api_gateway.util.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final JwtService jwtService;
    private final UserFeignClient feignClient;

    public String signup(RegistrationRequest request) {
        return feignClient.createUser(request).getData();
    }

    public void verifyOtp(OtpVerificationRequest request, HttpServletResponse httpResponse) {
        AuthUserDto dto = feignClient.verifyOtp(request).getData();
        String jwt = jwtService.generateToken(dto.getUserId(), Role.ROLE_USER.name());
        jwtService.addJwtToCookieResponse(jwt, httpResponse);
    }
}