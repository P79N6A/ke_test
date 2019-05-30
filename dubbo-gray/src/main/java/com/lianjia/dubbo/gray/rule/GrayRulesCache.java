package com.lianjia.dubbo.gray.rule;

import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.gray.filter.GrayConstants;
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
    private static Map<String, GrayRule> grayRuleHashMap = new ConcurrentHashMap<>();

    private GrayRulesCache() {
    }

    public static void updateGrayRules(List<GrayRule> grayRuleList) {
        Map map = new HashMap(grayRuleList.size());
        for (GrayRule grayRule : grayRuleList) {
            map.put(generateKey(grayRule.getServerIp(), String.valueOf(grayRule.getServerPort())), grayRule);
        }
        if (map != null && map.size() > 0) {
            grayRuleHashMap = map;
        }
    }

    public static boolean isNotEmpty() {
        return grayRuleHashMap != null && grayRuleHashMap.size() > 0;
    }

    public Map<String, GrayRule> getGrayRuleHashMap() {
        return grayRuleHashMap;
    }

    public static GrayRule getGrayRuleByServerAndPort(String server, String port) {
        if (grayRuleHashMap != null && grayRuleHashMap.size() > 0) {
            grayRuleHashMap.get(generateKey(server, port));
        }
        return null;
    }

    private static String generateKey(String serverIp, String serverPort) {
        return serverIp + GrayConstants.UNDER_LINE + serverPort;
    }

    public static String getStrOfContent() {
        return JSON.toJSONString(grayRuleHashMap);
    }
}
