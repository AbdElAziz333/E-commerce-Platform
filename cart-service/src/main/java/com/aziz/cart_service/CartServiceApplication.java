package com.aziz.cart_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CartServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CartServiceApplication.class);
        app.addInitializers(new DotenvInitializer());
        app.run(args);
    }
}