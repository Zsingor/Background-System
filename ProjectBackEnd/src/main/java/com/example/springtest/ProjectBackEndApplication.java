package com.example.springtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class ProjectBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectBackEndApplication.class, args);
    }

}
