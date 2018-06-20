package com.example.ZuulProxy;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import com.example.Zullfallback;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;

@SpringBootApplication
@Controller
@EnableZuulProxy
public class ZuulProxyApplication {
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulProxyApplication.class).web(true).run(args);
    }
    
    	@Bean
	public ZuulFallbackProvider route1ZuulFallbackProvider() {
		GenericZuulFallbackProvider route1ZuulFallback = new GenericZuulFallbackProvider();
		route1ZuulFallback.setRoute("route1");
		return route1ZuulFallback;
	}
	
	@Bean
	public ZuulFallbackProvider route2ZuulFallbackProvider() {
		GenericZuulFallbackProvider route2ZullFallback = new GenericZuulFallbackProvider();
		route2ZullFallback.setRoute("route2");
        route2ZuulFallback.setRawStatusCode(200);
        route2ZuulFallback.setStatusCode(HttpStatus.OK);
        route2ZuulFallback.setResponseBody("We are little busy. Comeback After Sometime");
		return portalZullFallback;
	}

    
}