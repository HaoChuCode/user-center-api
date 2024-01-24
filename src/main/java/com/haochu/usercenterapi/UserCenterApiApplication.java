package com.haochu.usercenterapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.haochu.usercenterapi.mapper")
public class UserCenterApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApiApplication.class, args);
    }
}
