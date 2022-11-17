package com.github.zshine.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class AuthApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class);
    }

    @GetMapping("health")
    public String health() {
        return "health check";
    }
}