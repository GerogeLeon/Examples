package com.example.helloauthconsumerfeign.controller;

import com.example.helloauthconsumerfeign.model.User;
import com.example.helloauthconsumerfeign.service.HelloAuthService;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-26
 */
@Import(FeignClientsConfiguration.class)
@RestController
public class HelloAuthFeignController {
    private HelloAuthService userAuthService;

    private HelloAuthService adminAuthService;

    @Autowired
    public HelloAuthFeignController(Decoder decoder, Encoder encoder, Client client, Contract contract){
        this.userAuthService= Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("user","123456"))
                .target(HelloAuthService.class,"http://hello-auth/");

        this.adminAuthService= Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin","123456"))
                .target(HelloAuthService.class,"http://hello-auth/");
    }

    @GetMapping("/user/{id}")
    public User findByIdUser(@PathVariable Long id){
        return this.userAuthService.findById(id);
    }


    @GetMapping("/admin/{id}")
    public User findByIdAdmin(@PathVariable Long id){
        return this.adminAuthService.findById(id);
    }


}
