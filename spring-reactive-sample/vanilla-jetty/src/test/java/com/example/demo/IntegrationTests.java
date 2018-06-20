package com.example.demo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.time.Duration;
import org.eclipse.jetty.server.Server;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 *
 * @author hantsy
 */
public class IntegrationTests {

    static Server jetty = null;

    WebTestClient rest;

    @BeforeClass
    public static void beforeAll() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class); 
        jetty = context.getBean(Server.class);
        jetty.start();
        //jetty.join();
    }

    @AfterClass
    public static void afterAll() throws Exception {
        if (jetty != null) {
            jetty.stop();
        }
    }

    @Before
    public void setup() {
        this.rest = WebTestClient
            .bindToServer()
            .responseTimeout(Duration.ofDays(1))
            .baseUrl("http://localhost:8080")
            .build();
    }

    @Test
    public void getAllPostsWillBeOk() throws Exception {
        this.rest
            .get()
            .uri("/posts")
            .exchange()
            .expectStatus().isOk();
    }

}
