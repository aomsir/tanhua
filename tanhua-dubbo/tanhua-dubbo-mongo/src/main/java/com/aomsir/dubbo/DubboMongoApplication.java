package com.aomsir.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: Aomsir
 * @Date: 2022/7/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@SpringBootApplication
@EnableAsync  // 支持异步多线程
public class DubboMongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboMongoApplication.class,args);
    }
}
