package com.aziz.auth_service.service;

import com.aziz.auth_service.config.JwtConfig;
import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.kafka.AuthPublisher;
import com.aziz.auth_service.model.RegistrationSession;
import com.aziz.auth_service.repository.RefreshTokenRepository;
import com.aziz.auth_service.repository.RegistrationSessionRepository;
import com.aziz.auth_service.repository.UserRepository;
import com.aziz.auth_service.request.VerifyOtpRequest;
import com.aziz.auth_service.request.RegistrationRequest;
import com.aziz.auth_service.util.exceptions.AlreadyExistsException;
import com.aziz.auth_service.util.exceptions.BadRequestException;
import com.aziz.auth_service.util.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationSessionRepository repository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthPublisher publisher;

    private static final int OTP_LENGTH = 6;
    private static final int VERIFICATION_ID_LENGTH = 16;
    private final SecureRandom sRandom = new SecureRandom();

    @Transactional
    public String signup(RegistrationRequest request) {
        log.debug("Attempting to register new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Cannot register user with email: {}, user already exists.", request.getEmail());
            throw new AlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        String otp = generateOtp();
        String verificationId = generateVerificationId();

        RegistrationSession session = RegistrationSession.builder()
                .id(verificationId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .otp(otp)
                .build();

        repository.save(session);
        publisher.publishOtp(request.getEmail(), otp);

        log.info("Registration session created for email: {}", request.getEmail());
        return verificationId;
    }

    @Transactional
    public void verifyOtp(VerifyOtpRequest request, HttpServletResponse httpResponse) {
        log.debug("Verifying OTP for verificationId: {}", request.getVerificationId());

        RegistrationSession registrationSession = repository
                .findById(request.getVerificationId())
                .orElseThrow(() -> new NotFoundException("Registration Session Expired"));

        if (!registrationSession.getEmail().equals(request.getEmail())) {
            throw new BadRequestException("Invalid Email");
        }

        if (!registrationSession.getOtp().equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }

        AuthUserDto authUser = userService.createUser(registrationSession);

        repository.deleteById(request.getVerificationId());

        issueTokens(authUser, httpResponse);
        publisher.publishWelcome(registrationSession.getEmail(), registrationSession.getFirstName());
    }

    private void issueTokens(AuthUserDto user, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUserId());
        String tokenId = jwtService.getJtiFromJwt(refreshToken);

        jwtService.addAccessTokenToCookie(accessToken, response);
        jwtService.addRefreshTokenToCookie(refreshToken, response);

        refreshTokenRepository.saveToken(user.getUserId(), tokenId, refreshToken, Instant.now().plusMillis(jwtConfig.getRefreshToken().getMaxAge()));
    }

    public String generateOtp() {
        return sRandom.ints(OTP_LENGTH, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    public String generateVerificationId() {
        byte[] bytes = new byte[VERIFICATION_ID_LENGTH];
        sRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}