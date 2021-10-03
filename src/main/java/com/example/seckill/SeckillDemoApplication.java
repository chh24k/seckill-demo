package com.example.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@MapperScan("com.example.seckill.mapper")
public class SeckillDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SeckillDemoApplication.class, args);
//        String[] beanDefinitionNames = run.getBeanDefinitionNames();
//        Arrays.stream(beanDefinitionNames).forEach(System.out :: println);
    }

}
