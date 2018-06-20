package com.example.eurekaserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		//SpringApplication.run(EurekaServerApplication.class, args);
		new SpringApplicationBuilder(EurekaServerApplication.class).web(true).run(args);
	}
}

