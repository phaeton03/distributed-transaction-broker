package com.example.correspodentbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
public class CorrespodentBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorrespodentBankApplication.class, args);
    }

}