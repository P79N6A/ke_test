package com.lianjia.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;

/**
 * @author liupinghe
 */

@Activate(group = Constants.CONSUMER, order = -19999)
public class ConsumerParamFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String ucId = RpcContext.getContext().getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);

        if(StringUtils.isEmpty(ucId)) {
            ucId = invocation.getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);
        }

        ucId = "123456";

        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        rpcInvocation.setAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID,ucId);

        return invoker.invoke(invocation);
    }
}
