package com.example.softwell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SoftwellApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftwellApplication.class, args);
	}

}
