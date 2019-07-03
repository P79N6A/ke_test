package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.common.MapUtil;
import com.lianjia.dubbo.gray.filter.params.AbstractParamCachableProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;
import com.lianjia.dubbo.gray.filter.params.UserDefinedParamCachableProcessor;

import java.util.Map;

/**
 * @author liupinghe
 */
@Activate(group = {com.alibaba.dubbo.common.Constants.CONSUMER},
        order = -10000)
public class BusinessParamConsumerFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //set ucid
        setBusinessParam(GrayConstants.FILTER_PARAM_UCID);
        //set cityCode
        setBusinessParam(GrayConstants.FILTER_PARAM_CITYCODE);
        //set curWorkCityCode
        setBusinessParam(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);
        //set attachmentKey
        setBusinessParam(GrayConstants.FILTER_PARAM_ATTACHMENT);

        return invoker.invoke(invocation);
    }

    private void setBusinessParam(String businessParamKey) {
        //用户自定义参数
        if (GrayConstants.FILTER_PARAM_ATTACHMENT.equals(businessParamKey)) {
            Map<String, String> attachmentParamsMap = UserDefinedParamCachableProcessor.getInstance().getValue();
            //自定义参数不为空
            if (!MapUtil.isEmpty(attachmentParamsMap)) {
                for (String attachmentKey : attachmentParamsMap.keySet()) {
                    RpcContext.getContext().setAttachment(attachmentKey,attachmentParamsMap.get(attachmentKey));
                }
            }
        } else {
            //固定的 ucid，cityCode，curWorkCityCode 参数
            AbstractParamCachableProcessor<String> processor = (AbstractParamCachableProcessor<String>) ParamProcessorFactory.getParamProcessByKey(businessParamKey);
            if (null != processor) {
                // 交互前重新设置 流量标识（例如ucid）, 避免信息丢失，consumer端逻辑
                if (StringUtils.isNotEmpty(processor.getValue())) {
                    RpcContext.getContext().setAttachment(businessParamKey, processor.getValue());
                }
            }
        }
    }

}
