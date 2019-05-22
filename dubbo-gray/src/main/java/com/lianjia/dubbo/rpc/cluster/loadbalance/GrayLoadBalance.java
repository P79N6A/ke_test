package com.lianjia.dubbo.rpc.cluster.loadbalance;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.alibaba.fastjson.JSON;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.domain.GrayRule;
import com.lianjia.dubbo.rpc.filter.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author liupinghe
 */
public class GrayLoadBalance extends AbstractLoadBalance {

    public static final Logger logger = LoggerFactory.getLogger(GrayLoadBalance.class);

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

//        // 灰度需要的参数必须下发，否则不进行灰度处理
//        if (checkGrayParam(isOpen, grayServerIp, grayServerPort, grayUcIdSet)) {
//            return doSelectGray(invokers, url, invocation);
//        }
        logger.info("缓存配置信息:{}", GrayRulesCache.getInstance().getGrayRuleHashMap());
        if (GrayRulesCache.getInstance().getGrayRuleHashMap().size() > 0)
            return doSelectGray(invokers, url, invocation);

        logger.info("路由策略：随机策略");
        return this.doRandomLoadBalanceSelect(invokers, url, invocation);
    }

    /**
     * 灰度路由选择
     *
     * @param invokers
     * @param url
     * @param invocation
     * @param <T>
     * @return
     */
    private <T> Invoker<T> doSelectGray(List<Invoker<T>> invokers, URL url, Invocation invocation) {

        logger.info("路由策略：灰度策略");

        List<Invoker<T>> excludeGrayInvokerList = new ArrayList<>();
        // 灰度机器invoker列表
        Invoker _invoker = null;
        GrayRule _grayRule = null;

        for (Invoker invoker : invokers) {
            String serverIp = invoker.getUrl().getIp();
            int serverPort = invoker.getUrl().getPort();

            String key = serverIp + "_" + serverPort;

            GrayRule grayRule = GrayRulesCache.getInstance().getGrayRuleHashMap().get(key);
            logger.info("GrayRule配置值：{}", JSON.toJSONString(grayRule));

            if (checkGrayParam(grayRule)) {
                // 灰度机器
                if (grayRule.isOpen() && grayRule.getServerIp().equals(serverIp)
                        && grayRule.getServerPort() == serverPort) {
                    _invoker = invoker;
                    _grayRule = grayRule;
                    continue;
                }
            }
            excludeGrayInvokerList.add(invoker);
        }

        if (_invoker != null) {
            String ucId = RpcContext.getContext().getAttachment(Constants.FILTER_PARAM_UCID);
            logger.info("ucId:{},ucIdSet:{}", ucId, _grayRule.getGrayUcIdSet());
            // 灰度流量
            if (StringUtils.isNotEmpty(ucId) && _grayRule.getGrayUcIdSet().contains(ucId)) {
                return _invoker;
            }
        }
        return this.doRandomLoadBalanceSelect(excludeGrayInvokerList, url, invocation);
    }

    private boolean checkGrayParam(GrayRule grayRule) {
        if (grayRule == null) return false;
        if (!grayRule.isOpen()) return false;
        if (StringUtils.isEmpty(grayRule.getServerIp())) return false;
        if (grayRule.getServerPort() <= 0) return false;
        if (grayRule.getGrayUcIdSet() == null && grayRule.getGrayUcIdSet().size() == 0) return false;
        return true;
    }

    public static final String NAME = "random";

    private final Random random = new Random();

    private <T> Invoker<T> doRandomLoadBalanceSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size(); // 总个数
        int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        for (int i = 0; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0
                    && weight != getWeight(invokers.get(i - 1), invocation)) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = random.nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < length; i++) {
                offset -= getWeight(invokers.get(i), invocation);
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        // 如果权重相同或权重为0则均等随机
        return invokers.get(random.nextInt(length));
    }


}
