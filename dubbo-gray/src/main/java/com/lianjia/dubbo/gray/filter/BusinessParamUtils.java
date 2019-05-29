package com.lianjia.dubbo.gray.filter;

import com.lianjia.dubbo.gray.filter.params.IParamProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;


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
        ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID).setValue(ucId);
    }

    /**
     * 对 cityCode 的埋点
     *
     * @param cityCode
     */
    public static void setCityCode(String cityCode) {
        ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CITYCODE).setValue(cityCode);
    }

    /**
     * 对 当前作业城市(curWorkCityCode) 的埋点
     * 由于 B端经纪人可以跨城作业，由此产生了 当前作业城市 的概念
     *
     * @param curWorkCityCode
     */
    public static void setCurWorkCityCode(String curWorkCityCode) {
        ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE).setValue(curWorkCityCode);
    }

    /**
     * 提供 外部埋点 逻辑处理完之后，调用clear 清空本次请求的缓存信息
     */
    public static void clear() {
        if (ParamProcessorFactory.getParamProcessMap() == null) {
            return;
        }
        for (String key : ParamProcessorFactory.getParamProcessMap().keySet()) {
            ParamProcessorFactory.getParamProcessByKey(key).clear();
        }
    }

    /**
     * 通过 流量标记的属性名称 获取 流量标记信息
     *
     * @param key {@link com.lianjia.dubbo.gray.filter.GrayConstants} 以 FILTER_PARAM_ 前缀开头的常量
     * @return
     */
    public static String getBusinessParamByKey(String key) {
        IParamProcessor paramProcess = ParamProcessorFactory.getParamProcessByKey(key);
        if (null != paramProcess) {
            return paramProcess.getValue();
        }
        return null;
    }

    /**
     * 通过 流量标记的属性名称 获取 流量标记信息
     *
     * @param key {@link com.lianjia.dubbo.gray.filter.GrayConstants} 以 FILTER_PARAM_ 前缀开头的常量
     * @return
     */
    public static void setBusinessParamByKey(String key, String value) {
        IParamProcessor paramProcess = ParamProcessorFactory.getParamProcessByKey(key);
        if (null != paramProcess) {
            paramProcess.setValue(value);
        }
    }
}
