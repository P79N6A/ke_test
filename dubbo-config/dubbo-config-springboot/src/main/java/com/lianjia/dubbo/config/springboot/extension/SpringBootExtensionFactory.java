package com.lianjia.dubbo.config.springboot.extension;

import com.alibaba.dubbo.common.extension.ExtensionFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class SpringBootExtensionFactory implements ExtensionFactory {

    private static final Set<ApplicationContext> contexts = new ConcurrentHashSet<ApplicationContext>();

    public static void addApplicationContext(ApplicationContext context) {
        contexts.add(context);
    }

    public static void removeApplicationContext(ApplicationContext context) {
        contexts.remove(context);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        for (ApplicationContext context : contexts) {
            if (context.containsBean(name)) {
                Object bean = context.getBean(name);
                if (type.isInstance(bean)) {
                    return (T) bean;
                }
            }
        }
        return null;
    }
}
