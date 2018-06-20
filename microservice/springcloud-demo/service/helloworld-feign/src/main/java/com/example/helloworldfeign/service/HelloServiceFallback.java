package com.example.helloworldfeign.service;

import com.example.helloworldfeign.model.HelloMessage;
import org.springframework.stereotype.Component;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-28
 */
@Component
public class HelloServiceFallback implements HelloService {
    @Override
    public HelloMessage hello() {
        HelloMessage helloMessage=new HelloMessage();
        helloMessage.setName("hello");
        helloMessage.setMessage("error occurs");
        return helloMessage;
    }
}
