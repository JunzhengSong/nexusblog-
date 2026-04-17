package com.nexusblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nexusblog.mapper")
public class NexusBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusBlogApplication.class, args);
    }
}
