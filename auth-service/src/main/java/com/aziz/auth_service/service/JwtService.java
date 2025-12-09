package com.aziz.auth_service.service;

import com.aziz.auth_service.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig config;
    private SecretKey accessTokenSecret;
    private SecretKey refreshTokenSecret;
    private JwtParser accessTokenParser;
    private JwtParser refreshTokenParser;

    @PostConstruct
    public void init() {
        accessTokenSecret = Keys.hmacShaKeyFor(config.getAccessToken().getSecret().getBytes(StandardCharsets.UTF_8));
        refreshTokenSecret = Keys.hmacShaKeyFor(config.getRefreshToken().getSecret().getBytes(StandardCharsets.UTF_8));
        accessTokenParser = Jwts.parser().verifyWith(accessTokenSecret).build();
        refreshTokenParser = Jwts.parser().verifyWith(refreshTokenSecret).build();
    }

    public String generateAccessToken(Long id, String role) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + config.getAccessToken().getMaxAge()))
                .signWith(accessTokenSecret)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .subject(id.toString())
                .claim("type", "refresh")
                .claim("jti", jti)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + config.getRefreshToken().getMaxAge()))
                .signWith(refreshTokenSecret)
                .compact();
    }

    public Long getUserIdFromJwt(String token, boolean isRefreshToken) {
        JwtParser parser = isRefreshToken ? refreshTokenParser : accessTokenParser;

        return Long.parseLong(parser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public String getRoleFromJwt(String token) {
        return accessTokenParser
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getJtiFromJwt(String token) {
        return refreshTokenParser
                .parseSignedClaims(token)
                .getPayload()
                .get("jti", String.class);
    }

    public String getTokenType(String token, boolean isRefreshToken) {
        JwtParser parser = isRefreshToken ? refreshTokenParser : accessTokenParser;

        return parser
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = accessTokenParser.parseSignedClaims(token).getPayload();
            String type = claims.get("type", String.class);
            return "access".equals(type);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = refreshTokenParser.parseSignedClaims(token).getPayload();
            String type = claims.get("type", String.class);
            return "refresh".equals(type);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void addAccessTokenToCookie(String jwt, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(config.getAccessToken().getCookieName(), jwt)
                .httpOnly(true)
                .secure(config.getAccessToken().isSecured())
                .maxAge(config.getAccessToken().getMaxAge() / 1000)
                .sameSite("Lax")
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void addRefreshTokenToCookie(String jwt, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(config.getRefreshToken().getCookieName(), jwt)
                .httpOnly(true)
                .secure(config.getRefreshToken().isSecured())
                .maxAge(config.getRefreshToken().getMaxAge() / 1000)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(config.getAccessToken().getCookieName(), "")
                .httpOnly(true)
                .secure(config.getAccessToken().isSecured())
                .maxAge(0)
                .sameSite("Lax")
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(config.getRefreshToken().getCookieName(), "")
                .httpOnly(true)
                .secure(config.getRefreshToken().isSecured())
                .maxAge(0)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String parseAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(config.getAccessToken().getCookieName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public String parseRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(config.getRefreshToken().getCookieName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}