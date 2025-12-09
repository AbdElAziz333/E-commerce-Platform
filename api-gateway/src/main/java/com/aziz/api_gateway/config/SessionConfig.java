package com.aziz.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "session")
public class SessionConfig {
    private String cookie;
    private long maxAge;
    private boolean secure;
    private String headerName;
}