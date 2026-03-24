package com.parent.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.parent.service.mapper")
@ComponentScan(basePackages = {"com.parent.service", "com.child.common"})
@EnableScheduling
public class ParentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParentApplication.class, args);
    }
}
