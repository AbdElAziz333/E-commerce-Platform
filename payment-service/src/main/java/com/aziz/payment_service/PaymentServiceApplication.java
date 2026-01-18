package com.aziz.payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PaymentServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PaymentServiceApplication.class);
        app.run(args);
	}
}