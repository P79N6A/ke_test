package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.common.GrayConstants;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:18 AM
 * @Version: 1.0
 */
public class ParamProcessorFactory {

    private static Map<String, IParamProcessor> paramProcessMap = new ConcurrentHashMap<>();

    static {
        paramProcessMap.put(GrayConstants.FILTER_PARAM_UCID, UcIdParamProcessor.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CITYCODE, CityCodeParamProcessor.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE, CurWorkCityParamProcessor.getInstance());
    }

    public static List<IParamProcessor> getAllParamProcess() {
        if (paramProcessMap == null) {
            return Collections.emptyList();
        }

        List result = new LinkedList();
        for (String key : paramProcessMap.keySet()){
            result.add(paramProcessMap.get(key));
        }

        return result;
    }

    //创建示例
    public static IParamProcessor getParamProcessByKey(String key) {
        return paramProcessMap.get(key);
    }
}
