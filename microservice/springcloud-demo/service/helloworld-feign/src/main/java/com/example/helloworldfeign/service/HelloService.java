package com.example.helloworldfeign.service;

import com.example.helloworldfeign.model.HelloMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-23
 */
@FeignClient(value="hello",fallbackFactory = HelloServiceFallbackFactory.class)//fallback = HelloServiceFallback.class)
public interface HelloService {

    @GetMapping("/message")
    HelloMessage hello();


}
