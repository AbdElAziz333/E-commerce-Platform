package com.aziz.user_service.service;

import com.aziz.user_service.config.JwtConfig;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + config.getMaxAge()))
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromJwt(String token) {
        return parser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        parser.parseSignedClaims(token);
        return true;
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