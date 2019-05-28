package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.filter.GrayConstants;

import java.util.*;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:18 AM
 * @Version: 1.0
 */
public class ParamProcessFactory {

    private static Map<String,IParamProcess> paramProcessMap = new HashMap<>();

    static {
        paramProcessMap.put(GrayConstants.FILTER_PARAM_UCID,UcIdParamProcess.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CITYCODE,CityCodeParamProcess.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE,CurWorkCityParamProcess.getInstance());
    }

    public static Map<String,IParamProcess> getParamProcessMap(){
        return paramProcessMap;
    }

    //创建示例
    public static IParamProcess getParamProcessByKey(String key) {
        return paramProcessMap.get(key);
    }
}
