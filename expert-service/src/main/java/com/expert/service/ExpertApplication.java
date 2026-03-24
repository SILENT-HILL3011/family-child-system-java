package com.expert.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.expert.service.mapper")
@ComponentScan(basePackages = {"com.expert.service", "com.child.common"})
public class ExpertApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpertApplication.class, args);
    }
}
