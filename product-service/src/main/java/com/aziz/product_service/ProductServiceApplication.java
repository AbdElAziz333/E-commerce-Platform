package com.aziz.product_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableElasticsearchAuditing
@EnableMongoRepositories(basePackages = "com.aziz.product_service.repository.mongo")
@EnableElasticsearchRepositories(basePackages = "com.aziz.product_service.repository.search")
public class ProductServiceApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductServiceApplication.class);
        app.addInitializers(new DotenvInitializer());
        app.run(args);
	}
}