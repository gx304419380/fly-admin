package com.fly.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.fly.admin.system.mapper")
@ServletComponentScan
public class FlyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAdminApplication.class, args);
    }

}
