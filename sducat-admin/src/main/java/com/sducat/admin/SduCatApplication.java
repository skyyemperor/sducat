package com.sducat.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * Created by skyyemperor on 2021-01-28 23:49
 * Description :
 */
@SpringBootApplication(scanBasePackages = "com.sducat.**")
@MapperScan("com.sducat.*.mapper")
public class SduCatApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.additional-location", "./config/,${user.home}/.sducat/");

        SpringApplication.run(SduCatApplication.class, args);
    }
}
