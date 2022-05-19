package com.airboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 * @author airboot
 */
@SpringBootApplication
public class AirbootApplication {
    
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AirbootApplication.class, args);
        System.out.println("---Airboot Server启动成功！---");
    }
}
