package com.example.customermanagementprojectteam11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CustomerManagementProjectTeam11Application {

    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementProjectTeam11Application.class, args);
    }

}
