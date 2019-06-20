package com.lianjia.dubbo.gray.rule.domain;

import java.util.Map;
import java.util.Set;

/**
 * @Description: 灰度规则信息
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/6/20 2:34 PM
 * @Version: 1.0
 */
public class RuleInfo {

    /**
     * 灰度账号
     */
    private Set<String> grayUcIdSet;

    /**
     * 当前城市
     */
    private Map<String, Integer> grayCityCodeMap;

    /**
     * 当前作业城市
     */
    private Map<String, Integer> grayCurWorkCityCodeMap;

    public Set<String> getGrayUcIdSet() {
        return grayUcIdSet;
    }

    public RuleInfo setGrayUcIdSet(Set<String> grayUcIdSet) {
        this.grayUcIdSet = grayUcIdSet;
        return this;
    }

    public Map<String, Integer> getGrayCityCodeMap() {
        return grayCityCodeMap;
    }

    public RuleInfo setGrayCityCodeMap(Map<String, Integer> grayCityCodeMap) {
        this.grayCityCodeMap = grayCityCodeMap;
        return this;
    }

    public Map<String, Integer> getGrayCurWorkCityCodeMap() {
        return grayCurWorkCityCodeMap;
    }

    public RuleInfo setGrayCurWorkCityCodeMap(Map<String, Integer> grayCurWorkCityCodeMap) {
        this.grayCurWorkCityCodeMap = grayCurWorkCityCodeMap;
        return this;
    }
}
