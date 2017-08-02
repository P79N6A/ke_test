/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.Status;
import com.alibaba.dubbo.demo.User;
import com.alibaba.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DemoServiceImpl implements DemoService {

    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        StringBuilder sb = new StringBuilder(name);
        for (int i=0;i<100;i++){
            sb.append("fjdsafkdjsfklsdajfkldsajfkljdsaklfjaksdflsdakjflksdafklsdafkdsaffdkasf"+i);
        }
        return "Hello " + sb.toString() + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

    @Override
    public User login(User user) {
        user.setPassword("123333");
        user.setUserName(user.getUserName() + " dddd");
        user.setSex("男fff");
        return user;
    }

    @Override
    public List<User> findUsers() {
        return null;
    }

    @Override
    public User saveUser(User user, boolean bb) {
        return user;
    }

    @Override
    public User findUser(Status status) {
        User user = new User();
        user.setPassword("123333");
        user.setUserName(user.getUserName() + " dddd");
        user.setSex("男fff");
        return user;
    }

}