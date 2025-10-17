package com.aziz.user_service.service;

import com.aziz.user_service.util.MailMessageSender;
import com.aziz.user_service.util.exceptions.InvalidOtpException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final MailMessageSender mailMessageSender;

    private final Duration OTP_DURATION = Duration.ofMinutes(10);
    private final String OTP_PREFIX = "OTP:";
    private final int OTP_LENGTH = 6;

    /**
     * generates OTP, verification id, stores them as key-value pairs, and sends notification to the email.
     *
     * @param email the email of the otp
     * @return verificationId
     * @author Aziz
     * */
    public String sendOtpToEmail(String email) {
        String otp = generateOtp();
        String verificationId = generateVerificationId();
        String value = email + ":" + otp;
        redisTemplate.opsForValue().set(getRedisKey(verificationId), value, OTP_DURATION);
        mailMessageSender.sendEmailVerification(email, otp);
        return verificationId;
    }

    /**
     * verifies OTP by using the verification id key
     *
     * @param verificationId the key of the record
     * @param otp the OTP to be verified
     * @author Aziz
     * */
    public String verifyOtp(String verificationId, String otp) {
        String redisKey = getRedisKey(verificationId);
        String storedValue = redisTemplate.opsForValue().get(redisKey);

        if (storedValue == null) {
            throw new InvalidOtpException("OTP expired or not found");
        }

        String[] parts = storedValue.split(":");
        String email = parts[0];
        String storedOtp = parts[1];

        if (!storedOtp.equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }

        redisTemplate.delete(redisKey);
        return email;
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

    private String getRedisKey(String verificationId) {
        return OTP_PREFIX + verificationId;
    }
}