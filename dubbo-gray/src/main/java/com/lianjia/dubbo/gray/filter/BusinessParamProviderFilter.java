package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.lianjia.dubbo.gray.filter.params.IParamProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;

/**
 * @author liupinghe
 */
@Activate(group = {com.alibaba.dubbo.common.Constants.PROVIDER},
        order = -10000)
public class BusinessParamProviderFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
            //set ucid
            setBusinessParam(GrayConstants.FILTER_PARAM_UCID);
            //set cityCode
            setBusinessParam(GrayConstants.FILTER_PARAM_CITYCODE);
            //set curWorkCityCode
            setBusinessParam(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);

            return invoker.invoke(invocation);
        } finally {
            BusinessParamUtils.clear();
        }
    }

    private void setBusinessParam(String businessParamKey) {
        String valueFromContext = RpcContext.getContext().getAttachment(businessParamKey);
        IParamProcessor processor = ParamProcessorFactory.getParamProcessByKey(businessParamKey);
        if (null != processor){
            if (StringUtils.isNotEmpty(valueFromContext)) {
                // 从RpcContext里获取 流量标识（例如：ucId） 并保存 provider端逻辑
                processor.setValue(valueFromContext);
            }
        }
    }

}
