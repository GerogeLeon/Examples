package com.javasampleapproach.webflux.functional.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.http.MediaType;

import com.javasampleapproach.webflux.model.Customer;
import com.javasampleapproach.webflux.repo.CustomerRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerHandler {
	
	private final CustomerRepository customerRepository;

	public CustomerHandler(CustomerRepository repository) {
		this.customerRepository = repository;
	}
	
	/**
	 * GET ALL Customers
	 */
    public Mono<ServerResponse> getAll(ServerRequest request) {
    	// fetch all customers from repository
    	Flux<Customer> customers = customerRepository.getAllCustomers();
    	
    	// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(customers, Customer.class);
    }
    
    /**
     * GET a Customer by ID 
     */
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
    	// parse path-variable
    	long customerId = Long.valueOf(request.pathVariable("id"));
    	
    	// build notFound response 
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		
		// get customer from repository 
		Mono<Customer> customerMono = customerRepository.getCustomerById(customerId);
		
		// build response
		return customerMono
                .flatMap(customer -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(customer)))
                .switchIfEmpty(notFound);
    }
    
    /**
     * POST a Customer
     */
    public Mono<ServerResponse> postCustomer(ServerRequest request) {
    	Mono<Customer> customer = request.bodyToMono(Customer.class);
        return ServerResponse.ok().build(customerRepository.saveCustomer(customer));
    }
    
    /**
     *	PUT a Customer
     */
    public Mono<ServerResponse> putCustomer(ServerRequest request) {
    	// parse id from path-variable
    	long customerId = Long.valueOf(request.pathVariable("id"));
    	
    	// get customer data from request object
    	Mono<Customer> customer = request.bodyToMono(Customer.class);
    	
		// get customer from repository 
		Mono<Customer> responseMono = customerRepository.putCustomer(customerId, customer);
		
		// build response
		return responseMono
                .flatMap(cust -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(cust)));
    }

    /**
     *	DELETE a Customer
     */
    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
    	// parse id from path-variable
    	long customerId = Long.valueOf(request.pathVariable("id"));
    	
    	// get customer from repository 
    	Mono<String> responseMono = customerRepository.deleteCustomer(customerId);
    	
    	// build response
		return responseMono
                .flatMap(strMono -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromObject(strMono)));
    }
}