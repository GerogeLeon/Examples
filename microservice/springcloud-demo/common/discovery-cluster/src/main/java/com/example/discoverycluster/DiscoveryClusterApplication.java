package com.example.discoverycluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class DiscoveryClusterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryClusterApplication.class, args);
	}
}
