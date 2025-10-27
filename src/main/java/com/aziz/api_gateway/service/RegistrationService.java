package com.aziz.api_gateway.service;

import com.aziz.api_gateway.client.UserFeignClient;
import com.aziz.api_gateway.dto.OtpVerificationRequest;
import com.aziz.api_gateway.dto.PendingUserDto;
import com.aziz.api_gateway.dto.RegistrationRequest;
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
        PendingUserDto dto = feignClient.verifyOtp(request).getData();
        String jwt = jwtService.generateToken(dto.getEmail());

        jwtService.addJwtToCookieResponse(jwt, httpResponse);
    }
}