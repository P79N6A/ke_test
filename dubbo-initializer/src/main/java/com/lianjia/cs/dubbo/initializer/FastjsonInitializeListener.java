package com.lianjia.cs.dubbo.initializer;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

/**
 * Created by chengtianliang on 2017/6/5.
 */
public class FastjsonInitializeListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
