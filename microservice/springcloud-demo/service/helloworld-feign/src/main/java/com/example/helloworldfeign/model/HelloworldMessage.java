package com.example.helloworldfeign.model;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-22
 */
public class HelloworldMessage {
    private HelloMessage hello;
    private WorldMessage word;

    public HelloMessage getHello() {
        return hello;
    }

    public void setHello(HelloMessage hello) {
        this.hello = hello;
    }

    public WorldMessage getWord() {
        return word;
    }

    public void setWord(WorldMessage word) {
        this.word = word;
    }
}
