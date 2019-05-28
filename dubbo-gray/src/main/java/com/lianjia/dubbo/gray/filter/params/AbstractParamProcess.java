package com.lianjia.dubbo.gray.filter.params;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamProcess implements ParamProcess {

    protected abstract void setRealValue(String value);

    @Override
    public void setValue(String value){
        setRealValue(parseValue(value));
    }

    private String parseValue(String value){
        //TODO：解析格式问题
        return null;
    }

}
