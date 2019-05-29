package com.lianjia.dubbo.gray.rule;

import com.lianjia.dubbo.gray.rule.domain.GrayRule;

import java.util.HashMap;
import java.util.List;
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

    public void updateGrayRules(List<GrayRule> grayRuleList) {
        Map map = new HashMap(grayRuleList.size());
        for (GrayRule grayRule : grayRuleList) {
            String key = grayRule.getServerIp() + "_" + grayRule.getServerPort();
            map.put(key, grayRule);
        }
        if (map != null && map.size() > 0) {
            grayRuleHashMap = map;
        }
    }

    public Map<String, GrayRule> getGrayRuleHashMap() {
        return grayRuleHashMap;
    }


}
