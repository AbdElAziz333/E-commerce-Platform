package com.aziz.user_service.repository;

import com.aziz.user_service.dto.PendingUserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PendingUserRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration PENDING_USER_TTL = Duration.ofMinutes(15);
    private static final String PENDING_USER_PREFIX = "PENDING_USER";

    public void save(PendingUserData data) {
        try {
            String key = getKey(data.getVerificationId());
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, json, PENDING_USER_TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize pending user data", e);
        }
    }

    public Optional<PendingUserData> findById(String verificationId) {
        try {
            String json = redisTemplate.opsForValue().get(getKey(verificationId));
            if (json == null) return Optional.empty();
            return Optional.of(objectMapper.readValue(json, PendingUserData.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize pending user data", e);
        }
    }

    public void delete(String verificationId) {
        redisTemplate.delete(getKey(verificationId));
    }

    private String getKey(String verificationId) {
        return PENDING_USER_PREFIX + ":" + verificationId;
    }
}
