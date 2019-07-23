package com.example.demo1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("第一次提交");
        System.out.println("什么情况");
        System.out.println();
        return  "hello";
    }
}

