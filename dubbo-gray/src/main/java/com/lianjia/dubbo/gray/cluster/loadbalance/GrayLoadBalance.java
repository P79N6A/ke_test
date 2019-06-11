package com.lianjia.dubbo.gray.cluster.loadbalance;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.lianjia.dubbo.gray.filter.GrayConstants;
import com.lianjia.dubbo.gray.filter.params.IParamProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;
import com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

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
        GrayRule _grayRule = null;

        for (Invoker invoker : invokers) {
            GrayRule grayRule = GrayRulesCache.getGrayRuleByServerAndPort(
                    invoker.getUrl().getIp(), String.valueOf(invoker.getUrl().getPort()));
            if (checkNullOfGrayParam(grayRule)) {
                // 灰度机器
                if (grayRule.isOpen() && grayRule.getServerIp().equals(invoker.getUrl().getIp())
                        && grayRule.getServerPort() == invoker.getUrl().getPort()) {
                    _invokers.add(invoker);
                    _grayRule = grayRule;
                    logger.info("have gray machine");
                    continue;
                }
            }
            excludeGrayInvokerList.add(invoker);
        }

        if (_invokers.size() > 0) {
            if (isGrayReq(_grayRule)) {
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
        if (StringUtils.isEmpty(grayRule.getServerIp())) return false;
        if (grayRule.getServerPort() <= 0) return false;
        return true;
    }

    private boolean isGrayReq(GrayRule _grayRule) {
        if (null == _grayRule) {
            return false;
        }
        //ketrace ucid
        logger.info("get ucid from ketrace :", TraceContext.getTargetValue(GrayConstants.FILTER_PARAM_UCID));

        // 灰度流量 ucId
        IParamProcessor ucIdProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID);
        if (ucIdProcessror.isGrayFlow(ucIdProcessror.getGrayValue(), _grayRule)) {
            return true;
        }

        // 灰度流量 cityCode
        IParamProcessor cityCodeProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CITYCODE);
        if (cityCodeProcessror.isGrayFlow(cityCodeProcessror.getGrayValue(), _grayRule)) {
            return true;
        }

        // 灰度流量 curWorkCityCode
        IParamProcessor curCityCodeProcessror = ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);
        if (curCityCodeProcessror.isGrayFlow(curCityCodeProcessror.getGrayValue(), _grayRule)) {
            return true;
        }

        return false;
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
