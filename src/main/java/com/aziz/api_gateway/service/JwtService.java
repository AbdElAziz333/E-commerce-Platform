package com.aziz.api_gateway.service;

import com.aziz.api_gateway.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig config;
    private SecretKey secretKey;
    private JwtParser parser;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(config.getSecret().getBytes(StandardCharsets.UTF_8));
        parser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateToken(Long id, String role) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + config.getMaxAge()))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        return Long.parseLong(parser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public String getRoleFromJwt(String token) {
        return parser
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void addJwtToCookieResponse(String jwt, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(config.getCookieName(), jwt)
                .httpOnly(true)
                .secure(config.isSecured())
                .maxAge(config.getMaxAge())
                .sameSite("Lax")
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}