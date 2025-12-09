package com.aziz.auth_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class TokenEncryptor {
    public String hashToken(String token) {
        String algorithm = "SHA-256";

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("failed to hash token, algorithm: " + algorithm + " not found.");
        }
    }

    public boolean compare(String rawToken, String hashedToken) {
        String hashedRawToken = hashToken(rawToken);

        return MessageDigest.isEqual(
                hashedRawToken.getBytes(StandardCharsets.UTF_8),
                hashedToken.getBytes(StandardCharsets.UTF_8));
    }
}