package com.aziz.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJpaAuditing
@SpringBootApplication
public class OrderServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OrderServiceApplication.class);
        app.run(args);
	}

    @GetMapping("/health")
    public String health() {
        return "message: OK";
    }
}