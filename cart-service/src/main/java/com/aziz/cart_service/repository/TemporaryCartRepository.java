package com.aziz.cart_service.repository;

import com.aziz.cart_service.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TemporaryCartRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String TEMPORARY_CART_PREFIX = "cart:session:";
    private static final Duration CART_TTL_DAYS = Duration.ofDays(90);

    public CartDto getTemporaryCart(String sessionId) {
        String key = buildKey(sessionId);
        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, CartDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot deserialize cart: " + e);
        }
    }

    public void addTemporaryCart(String sessionId, CartDto cart) {
        String key = buildKey(sessionId);

        try {
            String json = objectMapper.writeValueAsString(cart);
            redisTemplate.opsForValue().set(key, json, CART_TTL_DAYS);
        } catch (Exception e) {
            throw new RuntimeException("Cannot serialize cart for caching: ", e);
        }
    }

    public void invalidateCache(String sessionId) {
        if (sessionId == null) {
            return;
        }

        redisTemplate.delete(buildKey(sessionId));
    }

    public String buildKey(String sessionId) {
        return TEMPORARY_CART_PREFIX + sessionId;
    }
}