package com.aziz.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJpaAuditing
@SpringBootApplication
public class ApiGatewayApplication {
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayApplication.class);
        app.run(args);
	}

    @GetMapping("/health")
    public String health() {
        return "message: OK";
    }
}