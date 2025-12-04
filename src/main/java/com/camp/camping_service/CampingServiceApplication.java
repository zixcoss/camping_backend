package com.camp.camping_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CampingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampingServiceApplication.class, args);
	}

}
