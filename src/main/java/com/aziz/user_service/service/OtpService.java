package com.aziz.user_service.service;

import com.aziz.user_service.dto.OtpVerificationRequest;
import com.aziz.user_service.repository.OtpRepository;
import com.aziz.user_service.util.exceptions.InvalidOtpException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * Responsible for generating, verifying, OTPs and verification ids, and sends email notification.
 * */
@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository repository;

    private static final Duration OTP_DURATION = Duration.ofMinutes(5);
    private static final int OTP_LENGTH = 6;

    public OtpVerificationRequest createOtp(String email) {
        String otp = generateOtp();
        String verificationId = generateVerificationId();

        OtpVerificationRequest verification = new OtpVerificationRequest(verificationId, email, otp);
        repository.save(verification, OTP_DURATION);

        return verification;
    }

    public String verifyAndGetEmail(String verificationId, String otp) {
        OtpVerificationRequest verification = repository.findById(verificationId)
                .orElseThrow(() -> new NotFoundException("OTP expired or not found"));

        if (!verification.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }

        repository.delete(verificationId);
        return verification.getEmail();
    }

    private String generateOtp() {
        return new SecureRandom().ints(OTP_LENGTH, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    private String generateVerificationId() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}