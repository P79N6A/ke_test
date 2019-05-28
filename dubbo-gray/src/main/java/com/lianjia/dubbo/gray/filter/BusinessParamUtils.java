package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.lianjia.dubbo.gray.filter.params.IParamProcess;
import com.lianjia.dubbo.gray.filter.params.ParamProcessFactory;

import java.util.List;

/**
 * @author liupinghe
 */
public class BusinessParamUtils {

    public static void setUcId(String ucId) {
        ParamProcessFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID).setValue(ucId);
    }

    public static void setCityCode(String cityCode){
        ParamProcessFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CITYCODE).setValue(cityCode);
    }

    public static void setCurWorkCityCode(String curWorkCityCode){
        ParamProcessFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE).setValue(curWorkCityCode);
    }

    public static String getBusinessParamByKey(String key) {
        IParamProcess paramProcess = ParamProcessFactory.getParamProcessByKey(key);
        if (null != paramProcess) {
            return paramProcess.getValue();
        }
        return null;
    }

    public static void setBusinessParamByKey(String key, String value) {
        IParamProcess paramProcess = ParamProcessFactory.getParamProcessByKey(key);
        if (null != paramProcess) {
            paramProcess.setValue(value);
        }
    }


    public static void clear() {
        List<IParamProcess> list = ParamProcessFactory.getAllParamProcess();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        for (IParamProcess paramProcess : list){
            paramProcess.clear();
        }
    }
}
