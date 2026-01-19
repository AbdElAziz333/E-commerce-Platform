package com.aziz.auth_service.service;

import com.aziz.auth_service.repository.OtpRepository;
import com.aziz.auth_service.request.OtpVerificationRequest;
import com.aziz.auth_service.util.exceptions.InvalidOtpException;
import com.aziz.auth_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * Responsible for generating, verifying, OTPs and verification ids, and sends email notification.
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository repository;

    private static final int OTP_LENGTH = 6;

    /**
     * @param email user's email to register
     * */
    public OtpVerificationRequest createOtp(String email) {
        String otp = generateOtp();
        String verificationId = generateVerificationId();

        OtpVerificationRequest verification = new OtpVerificationRequest(verificationId, email, otp);
        repository.save(verification);

        return verification;
    }

    /**
     * @param verificationId The verificationId
     * @param otp the OTP
     * */
    public String verifyAndGetEmail(String verificationId, String otp) {
        log.debug("Attempting to verify OTP with OTP: {}", otp);

        OtpVerificationRequest verification = repository.findById(verificationId)
                .orElseThrow(() -> {
                    log.warn("Cannot verify OTP with verificationId: {}, OTP expired or not found", verificationId);
                    return new NotFoundException("OTP expired or not found");
                });

        if (!verification.getOtp().equals(otp)) {
            throw new InvalidOtpException("OTP is wrong, try a valid one");
        }

        repository.delete(verificationId);
        log.info("OTP with verificationId: {} and OTP: {}, successfully verified", verificationId, otp);
        return verification.getEmail();
    }

    /**
     * Generates OTP consists of 6 chars length, securely with SecureRandom instead of Random.
     * */
    private String generateOtp() {
        return new SecureRandom().ints(OTP_LENGTH, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    /**
     * Generates random 16 character long verification id.
     * */
    private String generateVerificationId() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}