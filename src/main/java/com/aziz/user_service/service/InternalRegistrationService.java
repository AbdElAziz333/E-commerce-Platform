package com.aziz.user_service.service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.kafka.UserPublisher;
import com.aziz.user_service.mapper.UserMapper;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.repository.UserRepository;
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
//    private final MailMessageSender mailMessageSender;
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
            log.error("Cannot register user with email: {}, user already exists.", request.getEmail());
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
        publisher.publishOtp(request.getEmail(), verification.getOtp());
//        publisher.publishUserOtpVerificationNotification(new UserOtpVerificationEvent(request.getEmail(), verification.getOtp()));
//        mailMessageSender.sendEmailVerification(request.getEmail(), verification.getOtp());

        log.info("User registered successfully with email: {}, verificationId: {}", request.getEmail(), verification.getVerificationId());
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
        log.debug("Attempting to verify OTP for user with email: {}, verificationId: {}", request.getEmail(), request.getVerificationId());

        // Verify OTP first
        String email = otpService.verifyAndGetEmail(request.getVerificationId(), request.getOtp());

        // Get pending user data from Redis
        PendingUserData pendingUser = pendingUserRepository.findById(request.getVerificationId())
                .orElseThrow(() -> {
                    log.error("Cannot verify OTP for user with email: {}, verificationId: {}, OTP not found", request.getEmail(), request.getVerificationId());
                    return new NotFoundException("Registration session expired or not found");
                });

        // Verify email matches
        if (!pendingUser.getEmail().equals(email)) {
            log.error("Cannot verify OTP for user with email: {}, email mismatch", request.getEmail());
            throw new IllegalStateException("Email mismatch in verification");
        }

        // Create actual user in database
        AuthUserDto authUser = userService.createUser(pendingUser);
        log.info("User verified OTP successfully with email: {}", request.getEmail());

        // Clean up Redis
        pendingUserRepository.delete(request.getVerificationId());

        // Send welcome email
//        mailMessageSender.sendWelcomeEmail(email, pendingUser.getFirstName());
//        publisher.publishWelcomeEmailNotification(new UserRegisteredEvent(email, pendingUser.getFirstName()));
        publisher.publishWelcome(email, pendingUser.getFirstName());

        return authUser;
    }
}