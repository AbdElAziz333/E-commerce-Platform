package com.aziz.auth_service.repository;

import com.aziz.auth_service.util.TokenEncryptor;
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
    private final TokenEncryptor encryptor;

    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(15);
    private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN";

    public void saveToken(Long userId, String tokenId, String refreshToken, Instant expiresAt) {
        String hashedToken = encryptor.hashToken(refreshToken);

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
        Map<Object, Object> data = redisTemplate.opsForHash().entries(getKey(tokenId));

        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable((String) data.get("hashed_token"));
    }

    public void delete(String tokenId) {
        String key = getKey(tokenId);
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

        if (!data.isEmpty()) {
            Long userId = Long.parseLong(data.get("user_id").toString());
            redisTemplate.opsForSet().remove("user:" + userId + ":tokens", tokenId);
        }

        redisTemplate.delete(key);
    }

    private static String getKey(String tokenId) {
        return REFRESH_TOKEN_PREFIX + ":" + tokenId;
    }
}