package com.example.helloworldfeign.service;

import com.example.helloworldfeign.model.WorldMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-23
 */
@FeignClient(value="world",fallback = WorldServiceFallback.class)
public interface WorldService {

    @GetMapping("/message")
    WorldMessage world();

}
