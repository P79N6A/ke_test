package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.gray.filter.GrayConstants;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamProcess implements IParamProcess {

    protected abstract boolean checkIsGrayFlow(String value, GrayRule _grayRule);

    @Override
    public boolean isGrayFlow(String value, GrayRule _grayRule) {
        return checkIsGrayFlow(getFilterParamValue(value), _grayRule);
    }

    /**
     * 由于 ketrace 在传递过程中，会将 String 转化成 Set，所以在获取需要解析
     *
     * @param value
     * @return
     */
    protected String getFilterParamValue(String value) {
        //FIXME：解析格式问题,比较单一
        String result = value;
        while (result.contains(GrayConstants.BRACKET_LEFT)) {
            com.alibaba.fastjson.JSONArray array = JSON.parseArray(result);
            if (array != null && array.size() > 0) {
                result = array.get(0).toString();
            }
        }
        return result;
    }

}
