package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * @Description: 公共缓存类
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamCachableProcessor extends AbstractParamProcessor {

    public static final Logger logger = LoggerFactory.getLogger(AbstractParamCachableProcessor.class);


    private final ThreadLocal<String> cache = new ThreadLocal<>();

    @Override
    public String getValue() {
        return cache.get();
    }

    @Override
    public void setValue(String cityCode) {
        cache.set(cityCode);
    }

    @Override
    public void clear() {
        cache.remove();
    }


}
