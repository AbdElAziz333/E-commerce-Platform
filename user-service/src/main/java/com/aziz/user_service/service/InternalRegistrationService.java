package com.aziz.user_service.service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.kafka.UserPublisher;
import com.aziz.user_service.mapper.UserMapper;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.request.OtpVerificationRequest;
import com.aziz.user_service.request.RegistrationRequest;
import com.aziz.user_service.util.exceptions.AlreadyExistsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalRegistrationService {
    private final OtpService otpService;
    private final UserService userService;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserPublisher publisher;

    /**
     * It stores user registration data in a redis database for temporary time (5 minutes) until the user verify its OTP.
     * @param request user's registration data (firstName, lastName, email, password, and phoneNumber) taken from auth-service.
     * @return the verification id for OTP user data in redis
     * @author Aziz
     * */
    @Transactional
    public String createUser(RegistrationRequest request) {
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

        log.info("Pending user saved for email: {}, verificationId: {}", request.getEmail(), verification.getVerificationId());
//        log.info("User registered successfully with email: {}, verificationId: {}", request.getEmail(), verification.getVerificationId());
        return verification.getVerificationId();
    }

    /**
     *
     * @param request request holds verificationId, OTP, and email.
     * @return AuthUserDto object, which holds user's email and role.
     * @author Aziz
     * */
    @Transactional
    public AuthUserDto verifyOtp(OtpVerificationRequest request) {
        //log.debug("Attempting to verify OTP for user with email: {}, verificationId: {}", request.getEmail(), request.getVerificationId());
        log.debug("Verifying OTP for email: {}, verificationId: {}", request.getEmail(), request.getVerificationId());

        // Verify OTP first
        String email = otpService.verifyAndGetEmail(request.getVerificationId(), request.getOtp());

        PendingUserData pendingUser = pendingUserRepository.findById(request.getVerificationId())
                .orElseThrow(() -> {
                    log.warn("Registration session expired or not found");
//                    log.warn("Cannot verify OTP for user with email: {}, verificationId: {}, OTP not found", request.getEmail(), request.getVerificationId());
                    return new NotFoundException("Registration session expired or not found");
                });

        // Verify email matches
        if (!pendingUser.getEmail().equals(email)) {
            log.warn("Email mismatch in verification");
//            log.warn("Cannot verify OTP for user with email: {}, email mismatch", request.getEmail());
            throw new IllegalStateException("Email mismatch in verification");
        }

        // Create actual user in database
        AuthUserDto authUser = userService.createUser(pendingUser);
        log.info("User verified OTP successfully with email: {}", request.getEmail());

        // Clean up Redis
        pendingUserRepository.delete(request.getVerificationId());

        publisher.publishWelcome(email, pendingUser.getFirstName());

        return authUser;
    }
}