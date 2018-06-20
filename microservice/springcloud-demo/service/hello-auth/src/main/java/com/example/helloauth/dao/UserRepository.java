package com.example.helloauth.dao;

import com.example.helloauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-26
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
