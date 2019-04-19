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
package com.alibaba.dubbo.demo.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.demo.User;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.rpc.filter.Constants;

public class DemoAction {
    
    private DemoService demoService;

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

	public void start() throws Exception {

        RpcContext.getContext().setAttachment(Constants.FILTER_PARAM_UCID,"123456");

        for (int i = 0; i < 10 ; i++) {
            try {
            	String hello = demoService.sayHello("world" + i);
                System.out.println("dfdfdfdfdfdfdfdfdfdfddfdfd" + hello);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}