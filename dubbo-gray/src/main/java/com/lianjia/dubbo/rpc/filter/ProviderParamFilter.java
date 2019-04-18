package com.lianjia.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;

/**
 * @author liupinghe
 */

@Activate(group = Constants.PROVIDER, order = -20000)
public class ProviderParamFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        String ucId = invocation.getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);

        if(StringUtils.isEmpty(ucId)) {
            ucId = RpcContext.getContext().getAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID);
        }


        RpcContext.getContext().setAttachment(com.lianjia.dubbo.rpc.filter.Constants.FILTER_PARAM_UCID, ucId);

        return invoker.invoke(invocation);
    }
}
