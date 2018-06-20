package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class WebfluxMultipartApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxMultipartApplication.class, args);
	}
}
