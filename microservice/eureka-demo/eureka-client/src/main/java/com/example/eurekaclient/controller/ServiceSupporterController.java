package com.example.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceSupporterController {
    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/test")
    public String test() {

        return "hello world";

    }
}
