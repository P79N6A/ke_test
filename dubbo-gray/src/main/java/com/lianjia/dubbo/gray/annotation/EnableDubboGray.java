package com.lianjia.dubbo.gray.annotation;

import com.lianjia.dubbo.gray.cluster.loadbalance.DubboGrayApolloConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description: 加载灰度相关配置
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/23 1:32 PM
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DubboGrayApolloConfig.class)
public @interface EnableDubboGray {
}
