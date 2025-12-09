package com.aziz.user_service.repository;

import com.aziz.user_service.dto.OtpVerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

/**
 * A repository for a redis database stores OTPs for users who are registered to fetch their data.
 * key is like OTP:verificationId (16) chars to fetch the OTP 6 chars.
 * like:
 * OTP:1234567890abcdef (key)-> test@example.com:123456 (value)
 * */
@Slf4j
@Repository
@RequiredArgsConstructor
public class OtpRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration OTP_DURATION = Duration.ofMinutes(5);

    private final String OTP_PREFIX = "OTP:";

    /**
     * @param verification
     * @author Aziz
     * */
    public void save(OtpVerificationRequest verification) {
        String key = getKey(verification.getVerificationId());
        String value = verification.getEmail() + ":" + verification.getOtp();
        redisTemplate.opsForValue().set(key, value, OTP_DURATION);
    }

    /**
     * @param verificationId
     * @author Aziz
     * */
    public Optional<OtpVerificationRequest> findById(String verificationId) {
        String value = redisTemplate.opsForValue().get(getKey(verificationId));

        if (value == null) {
            return Optional.empty();
        }

        String[] parts = value.split(":", 2);
        return Optional.of(new OtpVerificationRequest(verificationId, parts[0], parts[1]));
    }

    /**
     * @param verificationId
     * @author Aziz
     * */
    public void delete(String verificationId) {
        redisTemplate.delete(getKey(verificationId));
    }

    /**
     * @param verificationId
     * @author Aziz
     * */
    private String getKey(String verificationId) {
        return OTP_PREFIX + verificationId;
    }
}