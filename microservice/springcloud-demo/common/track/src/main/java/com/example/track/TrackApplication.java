package com.example.track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableZipkinServer
@EnableZipkinStreamServer
public class TrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackApplication.class, args);
	}
}
