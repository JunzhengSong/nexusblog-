package com.nexusblog;

import io.github.cdimascio.dotenv.Dotenv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nexusblog.mapper")
public class NexusBlogApplication {

    public static void main(String[] args) {
        // 加载 .env 文件中的环境变量
        Dotenv dotenv = Dotenv.configure()
                .directory("../")  // 项目根目录
                .ignoreIfMissing()
                .load();

        // 将 .env 中的变量设置为系统属性
        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });

        SpringApplication.run(NexusBlogApplication.class, args);
    }
}
