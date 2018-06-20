package com.example.hello.repository;

import com.example.hello.model.Info;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InfoRepository extends CrudRepository<Info,Long> {

    List<Info> findByName(String name);
}
