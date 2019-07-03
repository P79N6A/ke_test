package com.lianjia.dubbo.gray.filter.params;

import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public interface IParamProcessor {

    /**
     * 通过 流量标记名称 获取 值
     *
     * @param key 通过参数值
     * @return
     */
    String getGrayValue(String key);

    /**
     * @param key       流量标记名称
     * @param _ruleInfo 对应灰度规则
     * @return
     */
    boolean isGrayFlow(String key, RuleInfo _ruleInfo);
}
