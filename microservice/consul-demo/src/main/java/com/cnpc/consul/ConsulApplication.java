package com.cnpc.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConsulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulApplication.class, args);
	}
}
