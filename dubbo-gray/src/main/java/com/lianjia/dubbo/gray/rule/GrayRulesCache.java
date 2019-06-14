package com.lianjia.dubbo.gray.rule;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.common.MapUtil;
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
    private static volatile Map<String, Map<String, GrayRule>> grayRuleHashMap = new ConcurrentHashMap<>();

    private GrayRulesCache() {
    }

    public static void updateGrayRules(List<GrayRule> grayRuleList) {
        if (CollectionUtils.isEmpty(grayRuleList)) {
            return;
        }

        Map _grayRuleHashMap = new HashMap(grayRuleList.size());
        for (GrayRule grayRule : grayRuleList) {

            //haven't gray machine
            if (CollectionUtils.isEmpty(grayRule.getGrayServerIpSet())) {
                continue;
            }

            Map _appRulesMap = new HashMap();

            //contains appLication key
            if (_grayRuleHashMap.containsKey(grayRule.getApplication())) {
                _appRulesMap = grayRuleHashMap.get(grayRule.getApplication());
                if (_appRulesMap == null) {
                    _appRulesMap = new HashMap();
                }
            }

            for (String serverIp : grayRule.getGrayServerIpSet()) {
                _appRulesMap.put(generateKey(serverIp, String.valueOf(grayRule.getServerPort())), grayRule);
            }

            _grayRuleHashMap.put(grayRule.getApplication(), _appRulesMap);
        }

        if (!MapUtil.isEmpty(_grayRuleHashMap)) {
            grayRuleHashMap = _grayRuleHashMap;
        }
    }

    public static boolean isEmpty() {
        return MapUtil.isEmpty(grayRuleHashMap);
    }

    public static GrayRule getGrayRuleByServerAndPort(String applicationName, String server, String port) {
        if (isEmpty()) {
            return null;
        }

        Map<String, GrayRule> appRulesMap = getAppGrayRules(applicationName);
        if (MapUtil.isEmpty(appRulesMap)) {
            return null;
        }

        return appRulesMap.get(generateKey(server, port));

    }

    public static Map<String, GrayRule> getAppGrayRules(String applicationName) {
        return grayRuleHashMap.get(applicationName);
    }

    private static String generateKey(String serverIp, String serverPort) {
        return serverIp + GrayConstants.UNDER_LINE + serverPort;
    }

    public static String getStrOfContent() {
        return JSON.toJSONString(grayRuleHashMap);
    }
}
