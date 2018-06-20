package com.example.helloauthconsumerfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloAuthConsumerFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloAuthConsumerFeignApplication.class, args);
	}
}
