package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.gray.filter.GrayConstants;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamProcess implements IParamProcess {

    protected abstract void setRealValue(String value);

    @Override
    public void setValue(String value){
        setRealValue(parseValue(value));
    }

    private String parseValue(String value){
        //FIXME：解析格式问题,比较单一
        String result = value;
        while(result.contains(GrayConstants.BRACKET_LEFT)){
            com.alibaba.fastjson.JSONArray array = JSON.parseArray(result);
            if (array != null && array.size() > 0){
                result = array.get(0).toString();
            }
        }
        return result;
    }

}
