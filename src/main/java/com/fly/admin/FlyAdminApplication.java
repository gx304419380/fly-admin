package com.fly.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fly.admin.system.mapper")
public class FlyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAdminApplication.class, args);
    }

}
