package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.rule.domain.GrayRule;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public interface IParamProcess {

    String getValue();

    void setValue(String value);

    void clear();

    boolean isGrayFlow(String value, GrayRule _grayRule);
}
