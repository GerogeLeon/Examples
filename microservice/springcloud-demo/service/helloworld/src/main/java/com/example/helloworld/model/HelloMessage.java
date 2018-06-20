package com.example.helloworld.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloMessage {

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return name + ":" + message;
    }
}
