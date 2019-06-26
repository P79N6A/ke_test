package com.lianjia.dubbo.gray.cluster.loadbalance;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.common.MapUtil;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liupinghe
 */
class GrayRulesCache {

    /**
     * key：server+port
     * value：application-灰度应用名称
     */
    private static volatile Map<String, String> serverIpPortWithApplicationMap = new ConcurrentHashMap<>();

    /**
     * 灰度规则Map
     * key：application-灰度应用名称
     */
    private static volatile Map<String, GrayRule> grayRuleHashMap = new ConcurrentHashMap<>();


    private GrayRulesCache() {
    }

    /**
     * 更新缓存
     *
     * @param grayRuleList
     */
    public static void updateGrayRules(List<GrayRule> grayRuleList) {
        if (CollectionUtils.isEmpty(grayRuleList)) {
            return;
        }

        Map<String, GrayRule> _grayRuleHashMap = new HashMap(grayRuleList.size());
        Map<String, String> _serverIpPortWithApplicationMap = new HashMap();
        for (GrayRule grayRule : grayRuleList) {

            // 设置 application 与 GrayRule 的 对应关系
            if (null == _grayRuleHashMap.get(grayRule.getApplication())) {
                _grayRuleHashMap.put(grayRule.getApplication(), grayRule);
            }

            //haven't gray machine
            if (MapUtil.isEmpty(grayRule.getGrayServerIpMap())) {
                continue;
            }

            // 设置 serverIp+ port 与 application 的 对应关系
            for (String serverIp : grayRule.getGrayServerIpMap().keySet()) {
                if (null == _serverIpPortWithApplicationMap.get(
                        generateKey(serverIp, String.valueOf(grayRule.getServerPort())))) {
                    _serverIpPortWithApplicationMap.put(generateKey(serverIp, String.valueOf(grayRule.getServerPort())), grayRule.getApplication());
                }
            }
        }

        if (!MapUtil.isEmpty(_grayRuleHashMap)) {
            grayRuleHashMap = _grayRuleHashMap;
        }

        //serIpPortWithApplication is could be null ,which means no gray rules
        serverIpPortWithApplicationMap = _serverIpPortWithApplicationMap;
    }

    public static boolean isEmpty() {
        return MapUtil.isEmpty(grayRuleHashMap);
    }

    /**
     * 通过 server port 拿到缓存
     *
     * @param server
     * @param port
     * @return
     */
    public static GrayRule getGrayRuleByServerAndPort(String server, int port) {
        return getGrayRuleByServerAndPort(server, String.valueOf(port));
    }

    public static GrayRule getGrayRuleByServerAndPort(String server, String port) {
        if (isEmpty()) {
            return null;
        }

        String applicationName = serverIpPortWithApplicationMap.get(generateKey(server, port));
        if (StringUtils.isEmpty(applicationName)) {
            return null;
        }

        return grayRuleHashMap.get(applicationName);

    }

    private static String generateKey(String serverIp, String serverPort) {
        return serverIp + GrayConstants.UNDER_LINE + serverPort;
    }

    public static String getStrOfContent() {
        return JSON.toJSONString(grayRuleHashMap);
    }

    public static RuleInfo getRuleInfoByGrayRule(GrayRule grayRule, String serverIp) {
        if (null == grayRule) {
            return null;
        }
        return grayRule.getGrayServerIpMap().get(serverIp);
    }
}
