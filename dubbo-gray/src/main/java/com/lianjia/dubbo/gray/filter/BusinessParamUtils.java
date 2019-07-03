package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.lianjia.dubbo.gray.filter.params.*;


/**
 * @Description: 提供给外部埋点使用
 * @Author: liupinghe001@ke.com,qinxiaoyun001@ke.com
 * @Date: 2019/5/28 4:42 PM
 * @Version: 1.0
 */
public class BusinessParamUtils {

    /**
     * 对 ucid 的埋点
     *
     * @param ucId
     */
    public static void setUcId(String ucId) {
        UcIdParamProcessor.getInstance().setValue(ucId);
    }

    /**
     * 对 cityCode 的埋点
     *
     * @param cityCode
     */
    public static void setCityCode(String cityCode) {
        CityCodeParamProcessor.getInstance().setValue(cityCode);
    }

    /**
     * 对 当前作业城市(curWorkCityCode) 的埋点
     * 由于 B端经纪人可以跨城作业，由此产生了 当前作业城市 的概念
     *
     * @param curWorkCityCode
     */
    public static void setCurWorkCityCode(String curWorkCityCode) {
        CurWorkCityParamProcessor.getInstance().setValue(curWorkCityCode);
    }

    /**
     * 设置 自定义参数
     *
     * @param key
     * @param value
     */
    public static void setAttachment(String key, String value) {
        UserDefinedParamCachableProcessor.getInstance().setAttachment(key, value);
    }


    /**
     * 提供 外部埋点 逻辑处理完之后，调用clear 清空本次请求的缓存信息
     */
    public static void clear() {
        if (CollectionUtils.isEmpty(ParamProcessorFactory.getAllParamProcess())) {
            return;
        }
        for (IParamProcessor processor : ParamProcessorFactory.getAllParamProcess()) {
            if (processor instanceof UserDefinedParamCachableProcessor) {
                ((UserDefinedParamCachableProcessor) processor).clear();
            } else {
                ((AbstractParamCachableProcessor<String>) processor).clear();
            }
        }
    }

}
