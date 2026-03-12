package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void run(String... args) throws Exception {
        // Startup logic here (optional)
        System.out.println("Application started successfully!");
    }
}