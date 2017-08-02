package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.Status;
import com.alibaba.dubbo.demo.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by chengtianliang on 2017/7/25.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(Status.Registed,SerializerFeature.WriteClassName));
    }
}
