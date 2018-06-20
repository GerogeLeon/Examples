package com.example.helloworldfeign.service;

import com.example.helloworldfeign.model.HelloMessage;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-28
 */
@Component
public class HelloServiceFallbackFactory implements FallbackFactory<HelloService> {
    private final static Logger LOGGER= LoggerFactory.getLogger(HelloServiceFallbackFactory.class);
    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public HelloMessage hello() {
                //print the error
                LOGGER.error("fallback ,the result is:",throwable);
                HelloMessage helloMessage=new HelloMessage();
                helloMessage.setName("hello");
                helloMessage.setMessage("error occurs");
                return helloMessage;
            }
        };
    }
}
