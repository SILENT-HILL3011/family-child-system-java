package com.parent.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.parent.service.mapper")
public class ParentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParentApplication.class, args);
    }
}
