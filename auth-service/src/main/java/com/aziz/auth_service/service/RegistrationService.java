package com.aziz.auth_service.service;

import com.aziz.auth_service.config.JwtConfig;
import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.dto.PendingUserData;
import com.aziz.auth_service.kafka.AuthPublisher;
import com.aziz.auth_service.mapper.UserMapper;
import com.aziz.auth_service.repository.PendingUserRepository;
import com.aziz.auth_service.repository.RefreshTokenRepository;
import com.aziz.auth_service.repository.UserRepository;
import com.aziz.auth_service.request.OtpVerificationRequest;
import com.aziz.auth_service.request.RegistrationRequest;
import com.aziz.auth_service.util.exceptions.AlreadyExistsException;
import com.aziz.auth_service.util.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;
    private final OtpService otpService;
    private final UserService userService;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final AuthPublisher publisher;

    @Transactional
    public String signup(RegistrationRequest request) {
        log.debug("Attempting to register new user with email: {}", request.getEmail());

        if (repository.existsByEmail(request.getEmail())) {
            log.warn("Cannot register user with email: {}, user already exists.", request.getEmail());
            throw new AlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        // Generate OTP
        OtpVerificationRequest verification = otpService.createOtp(request.getEmail());

        // Map to pending user
        PendingUserData pendingUserData = mapper
                .registrationRequestToPendingUserData(
                        request,
                        encoder.encode(request.getPassword()),
                        verification.getVerificationId()
                );

        pendingUserRepository.save(pendingUserData);
        publisher.publishOtp(request.getEmail(), verification.getOtp());

        log.info("User registered successfully with email: {}, verificationId: {}", request.getEmail(), verification.getVerificationId());
        return verification.getVerificationId();
    }

    @Transactional
    public void verifyOtp(OtpVerificationRequest request, HttpServletResponse httpResponse) {
        log.debug("Attempting to verify OTP for user with email: {}, verificationId: {}", request.getEmail(), request.getVerificationId());

        // Verify OTP first
        String email = otpService.verifyAndGetEmail(request.getVerificationId(), request.getOtp());

        PendingUserData pendingUser = pendingUserRepository.findById(request.getVerificationId())
                .orElseThrow(() -> {
                    log.warn("Cannot verify OTP for user with email: {}, verificationId: {}, OTP not found", request.getEmail(), request.getVerificationId());
                    return new NotFoundException("Registration session expired or not found");
                });

        // Verify email matches
        if (!pendingUser.getEmail().equals(email)) {
            log.warn("Cannot verify OTP for user with email: {}, email mismatch", request.getEmail());
            throw new IllegalStateException("Email mismatch in verification");
        }

        // Create actual user in database
        AuthUserDto authUser = userService.createUser(pendingUser);
        log.info("User verified OTP successfully with email: {}", request.getEmail());

        // Clean up Redis
        pendingUserRepository.delete(request.getVerificationId());

        publisher.publishWelcome(email, pendingUser.getFirstName());

        String accessToken = jwtService.generateAccessToken(authUser.getUserId(), authUser.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(authUser.getUserId());

        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, httpResponse);
        jwtService.addRefreshTokenToCookie(refreshToken, httpResponse);

        refreshTokenRepository.saveToken(authUser.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));
    }
}