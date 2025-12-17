package com.aziz.payment_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "paymob")
public class PaymobConfig {
    private String apiKey;
    private String secretKey;
    private String publicKey;
    private String baseUrl;
    private Integer integrationId;
    private Integer iFrameId;
    private String hmacSecret;
}