package com.backend.ServerPrac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServerPracApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerPracApplication.class, args);
	}

}
