package com.lianjia.dubbo.rpc.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;

/**
 * @author liupinghe
 */
@Activate(group = {com.alibaba.dubbo.common.Constants.CONSUMER,
                    com.alibaba.dubbo.common.Constants.PROVIDER},
        order = -18888)
public class BusinessParamFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String ucId = RpcContext.getContext().getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);
        if (StringUtils.isNotEmpty(ucId)) {
            // 从RpcContext里获取ucId并保存
            BusinessParamUtils.setUcId(ucId);
        } else {
            // 交互前重新设置ucId, 避免信息丢失
            RpcContext.getContext().setAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID,
                    BusinessParamUtils.getUcId());
        }

        return invoker.invoke(invocation);
    }
}
