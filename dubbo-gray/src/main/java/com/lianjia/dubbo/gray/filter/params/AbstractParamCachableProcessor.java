package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * @Description: 公共缓存类
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamCachableProcessor<T> extends AbstractParamProcessor {

    public static final Logger logger = LoggerFactory.getLogger(AbstractParamCachableProcessor.class);

    private final ThreadLocal<T> cache = new ThreadLocal<>();

    public T getValue() {
        return cache.get();
    }

    public void setValue(T value) {
        cache.set(value);
    }

    public void clear() {
        cache.remove();
    }

}