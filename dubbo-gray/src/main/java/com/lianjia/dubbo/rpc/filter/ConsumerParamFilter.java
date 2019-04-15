package com.lianjia.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

/**
 * @author liupinghe
 */

@Activate(group = Constants.CONSUMER, order = -20000)
public class ConsumerParamFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String ucId = RpcContext.getContext().getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);

        ucId = "123456";
        RpcContext.getContext().setAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID, ucId);

        return invoker.invoke(invocation);
    }
}
