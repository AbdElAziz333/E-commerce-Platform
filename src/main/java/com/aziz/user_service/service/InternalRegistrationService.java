package com.aziz.user_service.service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.mapper.UserMapper;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.MailMessageSender;
import com.aziz.user_service.util.exceptions.AlreadyExistsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalRegistrationService {
    private final OtpService otpService;
    private final MailMessageSender mailMessageSender;
    private final UserService userService;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public String createUser(RegistrationRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        OtpVerificationRequest verification = otpService.createOtp(request.getEmail());

        PendingUserData pendingUserData = mapper
                .registrationRequestToPendingUserData(
                        request,
                        encoder.encode(request.getPassword()),
                        verification.getVerificationId()
                );

        pendingUserRepository.save(pendingUserData);
        mailMessageSender.sendEmailVerification(request.getEmail(), verification.getOtp());

        return verification.getVerificationId();
    }

    @Transactional
    public AuthUserDto verifyOtp(OtpVerificationRequest request) {
        // Verify OTP first
        String email = otpService.verifyAndGetEmail(request.getVerificationId(), request.getOtp());

        // Get pending user data from Redis
        PendingUserData pendingUser = pendingUserRepository.findById(request.getVerificationId())
                .orElseThrow(() -> new NotFoundException("Registration session expired or not found"));

        // Verify email matches
        if (!pendingUser.getEmail().equals(email)) {
            throw new IllegalStateException("Email mismatch in verification");
        }

        // Create actual user in database
        AuthUserDto authUser = userService.createUser(pendingUser);

        // Clean up Redis
        pendingUserRepository.delete(request.getVerificationId());

        // Send welcome email
        mailMessageSender.sendWelcomeEmail(email, pendingUser.getFirstName());

        return authUser;
    }
}