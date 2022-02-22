package com.example.financial_institution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinancialInstitutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialInstitutionApplication.class, args);
    }

}