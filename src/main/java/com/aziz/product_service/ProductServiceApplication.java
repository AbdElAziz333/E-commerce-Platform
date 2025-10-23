package com.aziz.product_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.aziz.product_service.repository.mongo")
@EnableElasticsearchRepositories(basePackages = "com.aziz.product_service.repository.search")
public class ProductServiceApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("./product-service").load();
		dotenv.entries().forEach((entry) -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ProductServiceApplication.class, args);
	}
}