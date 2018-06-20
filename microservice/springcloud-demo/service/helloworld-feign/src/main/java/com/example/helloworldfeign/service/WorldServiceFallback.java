package com.example.helloworldfeign.service;

import com.example.helloworldfeign.model.WorldMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-23
 */
@Component
public class WorldServiceFallback implements WorldService {

    @Override
    public WorldMessage world() {
        WorldMessage worldMessage=new WorldMessage();
        worldMessage.setMessage("world error occurs");
        return worldMessage;
    }
}
