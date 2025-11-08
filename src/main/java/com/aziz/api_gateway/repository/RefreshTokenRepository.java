package com.aziz.api_gateway.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration REFRESH_TOKEN_TTL = Duration.ofMinutes(10);

    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN";

    public void saveToken(Long userId, String tokenId, String hashedToken, Instant expiresAt) {
        Map<String, Object> data = Map.of(
                "user_id", userId,
                "hashed_token", hashedToken,
                "expires_at", expiresAt
        );

        String key = getKey(tokenId);
        redisTemplate.opsForHash().putAll(key, data);
        redisTemplate.expire(key, REFRESH_TOKEN_TTL);

        redisTemplate.opsForSet().add("user:" + userId + ":tokens", tokenId);
    }

    public Optional<String> findRefreshToken(String tokenId) {
        String key = getKey(tokenId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of((String) data.get("hashed_token"));
    }

    public void delete(String tokenId) {
        String key = getKey(tokenId);

        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

        if (!data.isEmpty()) {
            Long userId = (Long) data.get("user_id");
            redisTemplate.opsForSet().remove("user:" + userId + ":tokens", tokenId);
        }

        redisTemplate.delete(key);
    }

    private static String getKey(String tokenId) {
        return REFRESH_TOKEN_PREFIX + ":" + tokenId;
    }
}