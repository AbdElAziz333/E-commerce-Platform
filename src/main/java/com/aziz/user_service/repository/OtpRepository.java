package com.aziz.user_service.repository;

import com.aziz.user_service.dto.OtpVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OtpRepository {
    private final RedisTemplate<String, String> redisTemplate;

    private final String OTP_PREFIX = "OTP:";

    public void save(OtpVerificationRequest verification, Duration duration) {
        String key = getKey(verification.getVerificationId());
        String value = verification.getEmail() + ":" + verification.getOtp();
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public Optional<OtpVerificationRequest> findById(String verificationId) {
        String value = redisTemplate.opsForValue().get(getKey(verificationId));

        if (value == null) {
            return Optional.empty();
        }

        String[] parts = value.split(":", 2);
        return Optional.of(new OtpVerificationRequest(verificationId, parts[0], parts[1]));
    }

    public void delete(String verificationId) {
        redisTemplate.delete(getKey(verificationId));
    }

    private String getKey(String verificationId) {
        return OTP_PREFIX + verificationId;
    }
}