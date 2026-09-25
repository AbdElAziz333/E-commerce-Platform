package com.aziz.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {
    private TokenProperties accessToken;
    private TokenProperties refreshToken;

    @Setter
    @Getter
    public static class TokenProperties {
        private String secret;
        private String cookieName;
        private long maxAge;
        private boolean secured = false;
    }
}