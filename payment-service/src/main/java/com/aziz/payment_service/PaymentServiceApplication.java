package com.aziz.payment_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PaymentServiceApplication {
	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("./payment-service").load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(PaymentServiceApplication.class, args);
	}
}