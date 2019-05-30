package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.lianjia.dubbo.gray.filter.params.IParamProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;

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
        //set curWorkCityCode
        setBusinessParam(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);

        return invoker.invoke(invocation);
    }

    private void setBusinessParam(String businessParamKey) {
        String valueFromContext = RpcContext.getContext().getAttachment(businessParamKey);
        IParamProcessor processor = ParamProcessorFactory.getParamProcessByKey(businessParamKey);
        if (StringUtils.isNotEmpty(valueFromContext)) {
            // 从RpcContext里获取 流量标识（例如：ucId） 并保存
            processor.setValue(valueFromContext);
        } else {
            // 交互前重新设置 流量标识（例如ucid）, 避免信息丢失
            RpcContext.getContext().setAttachment(businessParamKey, processor.getValue());
        }
    }

    /**
     * 提供 外部埋点 逻辑处理完之后，调用clear 清空本次请求的缓存信息
     */
    public static void clear() {
        if (CollectionUtils.isEmpty(ParamProcessorFactory.getAllParamProcess())) {
            return;
        }
        for (IParamProcessor processor : ParamProcessorFactory.getAllParamProcess()) {
            processor.clear();
        }
    }
}
