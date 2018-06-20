package com.example.helloworldfeign.controller;

import com.example.helloworldfeign.model.HelloMessage;
import com.example.helloworldfeign.model.HelloworldMessage;
import com.example.helloworldfeign.model.WorldMessage;
import com.example.helloworldfeign.service.HelloService;
import com.example.helloworldfeign.service.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-23
 */
@RestController
public class HelloworldController {

    private static final Logger log = LoggerFactory.getLogger(HelloworldController.class);

    private static final String HELLO_SERVICE_NAME = "hello";

    private static final String WORLD_SERVICE_NAME = "world";

    @Autowired
    private HelloService helloService;

    @Autowired
    private WorldService worldService;

    @GetMapping("/")
    public String home() {
        return "hello world";
    }

    @GetMapping("/message")
    public HelloworldMessage getMessage() {
        HelloMessage hello = helloService.hello();
        WorldMessage world = worldService.world();
        HelloworldMessage helloworld = new HelloworldMessage();
        helloworld.setHello(hello);
        helloworld.setWord(world);
        log.debug("Result helloworld message:{}", helloworld);
        return helloworld;
    }

}
