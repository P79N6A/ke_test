package com.lianjia.dubbo.rpc.cluster.loadbalance;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.lianjia.dubbo.rpc.filter.Constants;

import java.util.*;

/**
 * @author liupinghe
 */
public class GrayLoadBalance extends AbstractLoadBalance {

    /**
     * 灰度开关
     */
    private static boolean isOpen = true;

    /**
     * 灰度机器Ip
     */
    private static String grayServerIp = "127.0.0.1";

    /**
     * 灰度白名单用户
     */
    private static Set<String> grayUcIdSet = new HashSet<>();

    static {
        grayUcIdSet.add("123456");
    }


    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

        // 灰度需要的参数必须下发，否则不进行灰度处理
        if (checkGrayParam(isOpen, grayServerIp, grayUcIdSet)) {
            return doSelectGray(invokers, url, invocation);
        }

        return this.doSelect(invokers, url, invocation);
    }

    private boolean checkGrayParam(boolean isOpen, String grayServerIp, Set<String> grayUcIdSet) {

        if (!isOpen) return false;
        if (StringUtils.isEmpty(grayServerIp)) return false;
        if (grayUcIdSet == null && grayUcIdSet.size() == 0) return false;
        return true;
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

        for (Invoker invoker : invokers) {
            String serverIp = invoker.getUrl().getIp();
            // 灰度机器
            if (StringUtils.isNotEmpty(serverIp) && grayServerIp.contains(serverIp)) {
                _invoker = invoker;
                continue;
            }
            excludeGrayInvokerList.add(invoker);
        }

        String ucId = RpcContext.getContext().getAttachment(Constants.FILTER_PARAM_UCID);

        // 灰度流量
        if (StringUtils.isNotEmpty(ucId) && grayUcIdSet.contains(ucId)) {
            return _invoker;
        }
        return this.doSelectGray(excludeGrayInvokerList, url, invocation);
    }
}
