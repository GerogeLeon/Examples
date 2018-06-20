/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 *
 * @author hantsy
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ApplicationTests {

    @Autowired
    ApplicationContext context;

    WebTestClient rest;

    @Before
    public void setup() {
        this.rest = WebTestClient
            .bindToApplicationContext(this.context)
            .configureClient()
            .build();
    }

    @Test
    public void searchPostsByKeyword_shouldBeOK() throws Exception {
        this.rest
            .get()
            .uri("/posts/search?q=post")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Post.class).hasSize(10);
    }

    @Test
    @Ignore
    public void countPostsByKeyword_shouldBeOK() throws Exception {
        this.rest
            .get()
            .uri("/posts/count?q=0")
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.count").isEqualTo(10);

        this.rest
            .get()
            .uri("/posts/count?q=5")
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.count").isEqualTo(19);

        this.rest
            .get()
            .uri("/posts/count?q=post")
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.count").isEqualTo(100);
    }


}
