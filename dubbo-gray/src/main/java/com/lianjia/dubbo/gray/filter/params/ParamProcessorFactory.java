package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.filter.GrayConstants;

import java.util.*;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:18 AM
 * @Version: 1.0
 */
public class ParamProcessorFactory {

    private static Map<String,IParamProcessor> paramProcessMap = new HashMap<>();

    static {
        paramProcessMap.put(GrayConstants.FILTER_PARAM_UCID,UcIdParamProcessor.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CITYCODE,CityCodeParamProcessor.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE,CurWorkCityParamProcessor.getInstance());
    }

    //创建示例
    public static IParamProcessor getParamProcessByKey(String key) {
        return paramProcessMap.get(key);
    }
}
