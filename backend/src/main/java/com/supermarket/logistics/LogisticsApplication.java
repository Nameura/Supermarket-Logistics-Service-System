package com.supermarket.logistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商超后勤服务系统主启动类
 */
@SpringBootApplication
@MapperScan("com.supermarket.logistics.mapper")
public class LogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
        System.out.println("==========================================");
        System.out.println("   商超后勤服务系统启动成功！");
        System.out.println("==========================================");
    }
}
