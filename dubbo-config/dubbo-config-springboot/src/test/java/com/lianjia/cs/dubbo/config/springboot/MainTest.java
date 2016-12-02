package com.lianjia.cs.dubbo.config.springboot;

import com.lianjia.cs.dubbo.config.springboot.entity.DubboProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@EnableAutoConfiguration
@ComponentScan(value = {"com.lianjia","com.alibaba.dubbo"})
@Configuration
public class MainTest implements CommandLineRunner {

    @Autowired
    private DubboProperty dubboProperty;

    public static void main(String[] args) {
        SpringApplication.run(MainTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(dubboProperty);
    }
}
