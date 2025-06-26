package com.chitova.florist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class FloristApplication {
    public static void main(String[] args) {
        SpringApplication.run(FloristApplication.class, args);
    }
}
