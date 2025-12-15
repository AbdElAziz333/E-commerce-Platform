package com.aziz.api_gateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        // Check if dev profile is active
        boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");
        
        if (isDev) {
            try {
                Dotenv dotenv = Dotenv.configure()
                    .directory("./auth-service")
                    .ignoreIfMissing()
                    .load();
                
                Map<String, Object> dotenvMap = new HashMap<>();
                dotenv.entries().forEach(entry -> 
                    dotenvMap.put(entry.getKey(), entry.getValue())
                );
                
                environment.getPropertySources().addFirst(
                    new MapPropertySource("dotenvProperties", dotenvMap)
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to load .env file", e);
            }
        }
    }
}