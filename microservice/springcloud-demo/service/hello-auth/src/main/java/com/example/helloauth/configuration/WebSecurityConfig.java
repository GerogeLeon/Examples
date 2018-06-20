package com.example.helloauth.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-26
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    private CustomUserDetailService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(this.userDetailService).passwordEncoder(this.passwordEncoder());
    }

    @Component
    class CustomUserDetailService implements UserDetailsService{
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
            if("user".equals(username)){
                return new SecurityUser("user","123456","user-role");
            }else if("admin".equals(username)){
                return new SecurityUser("admin","123456","admin-role");
            }else{
                return null;
            }
        }
    }

    class SecurityUser implements UserDetails{
        private static final long serialVersionUID=1L;

        public SecurityUser(String username,String password,String role){
            super();
            this.username=username;
            this.password=password;
            this.role=role;
        }

        public SecurityUser(){

        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<GrantedAuthority> authorities=new ArrayList<>();
            SimpleGrantedAuthority authority=new SimpleGrantedAuthority(this.role);
            authorities.add(authority);
            return authorities;
        }


        private Long id;
        private String username;
        private String password;
        private String role;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

}
