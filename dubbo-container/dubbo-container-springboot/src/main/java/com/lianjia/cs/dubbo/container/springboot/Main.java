package com.lianjia.cs.dubbo.container.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@EnableAutoConfiguration
@ComponentScan(value = {"com.lianjia", "com.alibaba.dubbo"})
@Configuration
public class Main implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {

    }
}
