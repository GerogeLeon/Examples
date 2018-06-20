package com.example.helloauth.controller;

import com.example.helloauth.dao.UserRepository;
import com.example.helloauth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-26
 */
@RestController
public class HelloAuthController {

    private static final Logger LOGGER=LoggerFactory.getLogger(HelloAuthController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        //权限控制
        Object principle= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof UserDetails){
            UserDetails user=(UserDetails)principle;
            Collection<? extends GrantedAuthority> collection=user.getAuthorities();
            for (GrantedAuthority ga : collection) {
                LOGGER.info("当前用户{}，角色是{}",user.getUsername(),ga.getAuthority());
            }
        }else{
            LOGGER.error("登录信息错误");
        }

        User findOne=this.userRepository.findOne(id);
        return findOne;
    }







}
