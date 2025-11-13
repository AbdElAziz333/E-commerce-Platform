package com.aziz.api_gateway.jwt;

import com.aziz.api_gateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JwtValidationService {
    private final JwtConfig config;
    private SecretKey accessTokenSecret;
    private JwtParser accessTokenParser;

    @PostConstruct
    public void init() {
        accessTokenSecret = Keys.hmacShaKeyFor(config.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8));
        accessTokenParser = Jwts.parser().verifyWith(accessTokenSecret).build();
    }

    public Long getUserIdFromJwt(String token) {
        return Long.parseLong(accessTokenParser.parseSignedClaims(token).getPayload().getSubject());
    }

    public String getRoleFromJwt(String token) {
        return accessTokenParser.parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = accessTokenParser.parseSignedClaims(token).getPayload();
            return "access".equals(claims.get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String parseAccessToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(config.getCookieName())) return cookie.getValue();
        }
        return null;
    }
}