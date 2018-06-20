package com.javasampleapproach.webflux.functional.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.javasampleapproach.webflux.functional.handler.CustomerHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.http.MediaType;

@Configuration
public class RoutingConfiguration {
	
    @Bean
    public RouterFunction<ServerResponse> monoRouterFunction(CustomerHandler customerHandler) {
        return route(GET("/api/customer").and(accept(MediaType.APPLICATION_JSON)), customerHandler::getAll)
        		.andRoute(GET("/api/customer/{id}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::getCustomer)
        		.andRoute(POST("/api/customer/post").and(accept(MediaType.APPLICATION_JSON)), customerHandler::postCustomer)
                .andRoute(PUT("/api/customer/put/{id}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::putCustomer)
                .andRoute(DELETE("/api/customer/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::deleteCustomer);
    }
    
}