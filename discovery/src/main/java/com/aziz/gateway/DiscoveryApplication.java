package com.aziz.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryApplication {
     public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DiscoveryApplication.class);
        app.run(args);
     }

    @GetMapping("/health")
    public String health() {
        return "message: OK";
    }
}
