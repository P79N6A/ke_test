package com.lianjia.dubbo.gray.rule;

import com.lianjia.dubbo.gray.rule.domain.GrayRule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liupinghe
 */
public class GrayRulesCache {

    /**
     * 灰度规则Map
     */
    private Map<String, GrayRule> grayRuleHashMap = new ConcurrentHashMap<>();

    private static GrayRulesCache grayRulesCache = new GrayRulesCache();

    private GrayRulesCache() {
    }

    public static GrayRulesCache getInstance() {
        return grayRulesCache;
    }

    public void updateGrayRules(GrayRule grayRule) {

        String key = grayRule.getServerIp() + "_" + grayRule.getServerPort();
        grayRuleHashMap.put(key, grayRule);

    }

    public Map<String, GrayRule> getGrayRuleHashMap() {
        return grayRuleHashMap;
    }


}
