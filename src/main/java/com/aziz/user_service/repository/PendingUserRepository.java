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
            String jsonValue = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, jsonValue, PENDING_USER_TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize pending user data", e);
        }
    }

    public Optional<PendingUserData> findById(String verificationId) {
        try {
            String key = getKey(verificationId);
            String jsonValue = redisTemplate.opsForValue().get(key);

            if (jsonValue == null) {
                return Optional.empty();
            }

            PendingUserData pendingUser = objectMapper.readValue(jsonValue, PendingUserData.class);
            return Optional.of(pendingUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize pending user data", e);
        }
    }

    public void delete(String verificationId) {
        String key = getKey(verificationId);
        redisTemplate.delete(key);
    }

    private String getKey(String verificationId) {
        return PENDING_USER_PREFIX + ":" + verificationId;
    }
}