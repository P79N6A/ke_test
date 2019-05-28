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

    public static Map<String,ParamProcess> paramProcessMap = new HashMap<>();

    static {
        paramProcessMap.put(GrayConstants.FILTER_PARAM_UCID,UcIdParamProcess.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CITYCODE,CityCodeParamProcess.getInstance());
        paramProcessMap.put(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE,CurWorkCityParamProcess.getInstance());
    }

    public static List<ParamProcess> getAllParamProcess(){
        List<ParamProcess> result = new LinkedList<>();
        for (String key : paramProcessMap.keySet()){
            result.add(paramProcessMap.get(key));
        }
        return result;
    }

    //创建示例
    public static ParamProcess getParamProcessByKey(String key) {
        return paramProcessMap.get(key);
    }
}
