package com.lianjia.dubbo.gray.cluster.loadbalance;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.common.MapUtil;
import com.lianjia.dubbo.gray.filter.params.IParamProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;
import com.lianjia.dubbo.gray.filter.params.UserDefinedParamCachableProcessor;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

import java.util.*;

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
        logger.debug("gray rules info:{}", GrayRulesCache.getStrOfContent());
        if (!GrayRulesCache.isEmpty())
            return doSelectGray(invokers, url, invocation);

        logger.debug("loadbablance：random");
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

        List<Invoker<T>> excludeGrayInvokerList = new ArrayList<>();
        // 灰度机器invoker列表
        List<Invoker<T>> _invokers = new ArrayList<>();
        RuleInfo _ruleInfo = null;

        for (Invoker invoker : invokers) {
            GrayRule grayRule = GrayRulesCache.getGrayRuleByServerAndPort(invoker.getUrl().getIp(), invoker.getUrl().getPort());
            if (checkNullOfGrayParam(grayRule)) {
                // 灰度机器
                _invokers.add(invoker);
                _ruleInfo = GrayRulesCache.getRuleInfoByGrayRule(grayRule, invoker.getUrl().getIp());
                logger.info("have gray machine");
                continue;
            }
            excludeGrayInvokerList.add(invoker);
        }

        if (_invokers.size() > 0) {
            if (isGrayReq(_ruleInfo)) {
                logger.info("loadbablance：gray");
                if (_invokers.size() == 1) {
                    return _invokers.get(0);
                }
                return this.doRandomLoadBalanceSelect(_invokers, url, invocation);
            }
        }
        logger.debug("loadbablance：random");
        return this.doRandomLoadBalanceSelect(excludeGrayInvokerList, url, invocation);
    }

    /**
     * 只校验灰度机器相关
     *
     * @param grayRule
     * @return
     */
    private boolean checkNullOfGrayParam(GrayRule grayRule) {
        if (grayRule == null) return false;
        if (!grayRule.isOpen()) return false;
        if (MapUtil.isEmpty(grayRule.getGrayServerIpMap())) return false;
        if (grayRule.getServerPort() <= 0) return false;
        return true;
    }

    private boolean isGrayReq(RuleInfo _ruleInfo) {
        if (null == _ruleInfo) {
            return false;
        }

        // 灰度流量 ucId
        IParamProcessor ucIdProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID);
        if (ucIdProcessror.isGrayFlow(GrayConstants.FILTER_PARAM_UCID, _ruleInfo)) {
            return true;
        }

        // 灰度流量 cityCode
        IParamProcessor cityCodeProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CITYCODE);
        if (cityCodeProcessror.isGrayFlow(GrayConstants.FILTER_PARAM_CITYCODE, _ruleInfo)) {
            return true;
        }

        // 灰度流量 curWorkCityCode
        IParamProcessor curCityCodeProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);
        if (curCityCodeProcessror.isGrayFlow(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE, _ruleInfo)) {
            return true;
        }

        //自定义参数
        IParamProcessor userAttachmentProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_ATTACHMENT);
        Set<String> attachmentKeySet = getAttachmentKeySet(_ruleInfo);
        if (CollectionUtils.isNotEmpty(attachmentKeySet)) {
            for (String attachmentKey : attachmentKeySet) {
                if (userAttachmentProcessror.isGrayFlow(attachmentKey, _ruleInfo)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Set<String> getAttachmentKeySet(RuleInfo ruleInfo) {
        if (MapUtil.isEmpty(ruleInfo.getAttachmentsMap())) {
            return null;
        }

        Set<String> rpcAttachmentKeySet = RpcContext.getContext().getAttachments().keySet();
        if (CollectionUtils.isEmpty(rpcAttachmentKeySet)){
            return null;
        }

        Set<String> result = new HashSet<>();
        for (String key : ruleInfo.getAttachmentsMap().keySet()){
            if (rpcAttachmentKeySet.contains(UserDefinedParamCachableProcessor.getInstance().generageKey(key))){
                result.add(key);
            }
        }
        return result;
    }

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
