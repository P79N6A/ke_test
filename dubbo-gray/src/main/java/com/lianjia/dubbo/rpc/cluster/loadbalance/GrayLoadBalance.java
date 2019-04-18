package com.lianjia.dubbo.rpc.cluster.loadbalance;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.domain.GrayRule;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.rpc.filter.Constants;

import java.util.*;

/**
 * @author liupinghe
 */
public class GrayLoadBalance extends AbstractLoadBalance {


    static {
        GrayRule grayRule = new GrayRule();
        grayRule.setOpen(true);
        grayRule.setServerIp("10.33.76.22");
        grayRule.setServerPort(20881);
        Set<String> ucIdSet = new HashSet<>();
        ucIdSet.add("123456");
        grayRule.setGrayUcIdSet(ucIdSet);
        GrayRulesCache.getInstance().updateGrayRules(grayRule);
    }

//    /**
//     * 灰度开关
//     */
//    private static boolean isOpen = true;
//
//    /**
//     * 灰度机器Ip
//     */
//    private static String grayServerIp = "10.33.76.22";
//
//    /**
//     * 灰度机器端口
//     */
//    private static int grayServerPort = 20881;
//
//
//    /**
//     * 灰度白名单用户
//     */
//    private static Set<String> grayUcIdSet = new HashSet<>();
//
//    static {
//        grayUcIdSet.add("123456");
//    }


    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

//        // 灰度需要的参数必须下发，否则不进行灰度处理
//        if (checkGrayParam(isOpen, grayServerIp, grayServerPort, grayUcIdSet)) {
//            return doSelectGray(invokers, url, invocation);
//        }

        if (GrayRulesCache.getInstance().getGrayRuleHashMap().size() > 0)
            return doSelectGray(invokers, url, invocation);

        return this.doSelect(invokers, url, invocation);
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
        Invoker _invoker = null;
        GrayRule _grayRule = null;

        for (Invoker invoker : invokers) {
            String serverIp = invoker.getUrl().getIp();
            int serverPort = invoker.getUrl().getPort();

            String key = serverIp + "_" + serverPort;

            GrayRule grayRule = GrayRulesCache.getInstance().getGrayRuleHashMap().get(key);

            if(checkGrayParam(grayRule)) {
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

        if(_invoker != null) {
            String ucId = RpcContext.getContext().getAttachment(Constants.FILTER_PARAM_UCID);
            // 灰度流量
            if (StringUtils.isNotEmpty(ucId) && _grayRule.getGrayUcIdSet().contains(ucId)) {
                return _invoker;
            }
        }
        return this.doSelectGray(excludeGrayInvokerList, url, invocation);
    }

    private boolean checkGrayParam(GrayRule grayRule) {
        if (grayRule == null) return false;
        if (!grayRule.isOpen()) return false;
        if (StringUtils.isEmpty(grayRule.getServerIp())) return false;
        if (grayRule.getServerPort() <= 0) return false;
        if (grayRule.getGrayUcIdSet() == null && grayRule.getGrayUcIdSet().size() == 0) return false;
        return true;
    }
}
