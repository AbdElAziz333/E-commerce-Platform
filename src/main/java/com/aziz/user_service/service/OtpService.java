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

    private final Duration OTP_DURATION = Duration.ofMinutes(10);
    private final int OTP_LENGTH = 6;

    /**
     * generates OTP, verification id, stores them as key-value pairs, and sends notification to the email.
     *
     * @param email the email of the otp
     * @return verificationId
     * @author Aziz
     * */
    public OtpVerificationRequest createOtp(String email) {
        String otp = generateOtp();
        String verificationId = generateVerificationId();

        OtpVerificationRequest verification = new OtpVerificationRequest(verificationId, email, otp);
        repository.save(verification, OTP_DURATION);

        return verification;
    }

    /**
     * verifies OTP by using the verification id key
     *
     * @param verificationId the key of the record
     * @param otp the OTP to be verified
     * @author Aziz
     * */
    public String verifyAndGetEmail(String verificationId, String otp) {
        OtpVerificationRequest verification = repository.findById(verificationId)
                .orElseThrow(() -> new NotFoundException("OTP expired or not found"));

        if (!verification.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid Otp");
        }

        repository.delete(verificationId);
        return verification.getEmail();
    }

    /**
     * Generates OTP of 6 characters-length.
     * @return OTP String
     * @author Aziz
     * */
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        return random.ints(OTP_LENGTH, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    /**
     * Generates verification id of 22 characters-length.
     * @return verificationId URL-safe base64 string without padding
     * @author Aziz
     * */
    private String generateVerificationId() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}