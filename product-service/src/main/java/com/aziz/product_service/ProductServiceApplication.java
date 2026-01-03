package com.aziz.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProductServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.run(args);
	}
}