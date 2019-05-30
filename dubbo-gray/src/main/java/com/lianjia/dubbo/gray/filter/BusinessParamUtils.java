package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.rpc.RpcContext;
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
        RpcContext.getContext().setAttachment(GrayConstants.FILTER_PARAM_UCID, ucId);
    }

    /**
     * 对 cityCode 的埋点
     *
     * @param cityCode
     */
    public static void setCityCode(String cityCode) {
        RpcContext.getContext().setAttachment(GrayConstants.FILTER_PARAM_CITYCODE, cityCode);
    }

    /**
     * 对 当前作业城市(curWorkCityCode) 的埋点
     * 由于 B端经纪人可以跨城作业，由此产生了 当前作业城市 的概念
     *
     * @param curWorkCityCode
     */
    public static void setCurWorkCityCode(String curWorkCityCode) {
        RpcContext.getContext().setAttachment(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE, curWorkCityCode);
    }

    public static void clear(){
        if (CollectionUtils.isEmpty(ParamProcessorFactory.getAllParamProcess())) {
            return;
        }
        for (IParamProcessor processor : ParamProcessorFactory.getAllParamProcess()){
            processor.clear();
        }
    }

}
