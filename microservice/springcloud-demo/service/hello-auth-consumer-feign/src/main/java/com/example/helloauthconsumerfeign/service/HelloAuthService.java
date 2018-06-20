package com.example.helloauthconsumerfeign.service;

import com.example.helloauthconsumerfeign.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-23
 */
//@FeignClient(value="hello-auth")
public interface HelloAuthService {

    @GetMapping("/{id}")
    User findById(@PathVariable("id") Long id);

}
