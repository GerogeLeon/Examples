package com.example.hello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 描述信息实体
 *
 * @author billjiang billjiang
 * @create 17-8-22
 */

@Entity
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String value;


    public Info(){ }

    public Info(String name,String value){
        this.name=name;
        this.value=value;
    }


    @Override
    public String toString(){
        return String.format("HelloService %s - %s",name,value);
    }
}
