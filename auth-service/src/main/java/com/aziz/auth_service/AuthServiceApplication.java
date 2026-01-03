package com.aziz.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthServiceApplication.class);
        app.run(args);
	}
}