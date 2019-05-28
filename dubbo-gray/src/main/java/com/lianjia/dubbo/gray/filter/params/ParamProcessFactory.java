package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.filter.GrayConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:18 AM
 * @Version: 1.0
 */
public class ParamProcessFactory {

    public static List<ParamProcess> list = new ArrayList<>();

    static {
        list.add(CurWorkCityParamProcess.getInstance());
        list.add(CityCodeParamProcess.getInstance());
        list.add(UcIdParamProcess.getInstance());
    }

    public static List<ParamProcess> getAllParamProcess(){
        return list;
    }

    //创建示例
    public static ParamProcess getParamProcessByKey(String key) {
        switch (key) {
            case GrayConstants.FILTER_PARAM_UCID:
                return UcIdParamProcess.getInstance();
            case GrayConstants.FILTER_PARAM_CITYCODE:
                return CityCodeParamProcess.getInstance();
            case GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE:
                return CurWorkCityParamProcess.getInstance();
            default:
                return null;
        }
    }
}
