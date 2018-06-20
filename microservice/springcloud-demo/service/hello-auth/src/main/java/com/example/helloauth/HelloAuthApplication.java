package com.example.helloauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloAuthApplication.class, args);
	}
}
