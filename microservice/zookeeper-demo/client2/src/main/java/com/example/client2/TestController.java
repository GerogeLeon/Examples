package com.example.client2;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @RequestMapping(name="HelloService",method = RequestMethod.GET,path = "/hello")
    public String hello(){
        return "Hello  client2" ;
    }

    @RequestMapping(name="TestService",method = RequestMethod.GET,path = "/test")
    public String test(){
        return "test";
    }
}
