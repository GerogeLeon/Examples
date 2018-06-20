package com.example.world.controller;

import com.example.world.model.WorldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-22
 */
@RestController
public class WorldController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/")
    public String home(){
        return "world";
    }

    @GetMapping("/message")
    public WorldMessage getMessage(){
        WorldMessage worldMessage=new WorldMessage();
        worldMessage.setMessage(getLocalInstanceInfo());
        return worldMessage;
    }


    private String getLocalInstanceInfo() {
       ServiceInstance serviceInstance=discoveryClient.getLocalServiceInstance();
       return serviceInstance.getServiceId()+"@"+serviceInstance.getHost()+":"+serviceInstance.getPort();

    }
}
