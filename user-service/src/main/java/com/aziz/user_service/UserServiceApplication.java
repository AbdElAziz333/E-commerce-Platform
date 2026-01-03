package com.aziz.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.run(args);
	}
}