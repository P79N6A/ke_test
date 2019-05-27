package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;

/**
 * @author liupinghe
 */
//@Activate(group = {com.alibaba.dubbo.common.Constants.CONSUMER,
//                    com.alibaba.dubbo.common.Constants.PROVIDER},
//        order = -10000)
public class BusinessParamFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //set ucid
        setBusinessParam(GrayConstants.FILTER_PARAM_UCID);
        //set cityCode
        setBusinessParam(GrayConstants.FILTER_PARAM_CITYCODE);

        return invoker.invoke(invocation);
    }

    private void setBusinessParam(String businessParamKey) {
        String businessParamValue = RpcContext.getContext().getAttachment(businessParamKey);
        if (StringUtils.isNotEmpty(businessParamValue)) {
            // 从RpcContext里获取 流量标识（例如：ucId） 并保存
            BusinessParamUtils.setBusinessParamByKey(businessParamKey, businessParamValue);
        } else {
            // 交互前重新设置 流量标识（例如ucid）, 避免信息丢失
            RpcContext.getContext().setAttachment(businessParamKey,
                    BusinessParamUtils.getBusinessParamByKey(businessParamKey));
        }
    }
}
