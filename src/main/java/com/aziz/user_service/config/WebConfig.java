package com.aziz.user_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "web")
@Configuration
@Getter
@Setter
public class WebConfig {
    private String frontendUrl;
}