package com.example.hello;

import com.example.hello.model.Info;
import com.example.hello.repository.InfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;


@EnableDiscoveryClient
@SpringBootApplication
public class HelloApplication {

	private static final Logger log= LoggerFactory.getLogger(HelloApplication.class);
	public static final String KEY="Database";
	public static final String VALUE="MYSQL FROM BILLJIANG";

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(InfoRepository repository){
		return (args) -> {
			repository.save(new Info(KEY,VALUE));
		};
	}
}

