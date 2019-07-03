package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.filter.params.AbstractParamCachableProcessor;
import com.lianjia.dubbo.gray.filter.params.ParamProcessorFactory;
import com.lianjia.dubbo.gray.filter.params.UserDefinedParamCachableProcessor;

import java.util.Set;

/**
 * @author liupinghe
 */
@Activate(group = {com.alibaba.dubbo.common.Constants.PROVIDER},
        order = -10000)
public class BusinessParamProviderFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            //set ucid
            setBusinessParam(GrayConstants.FILTER_PARAM_UCID);
            //set cityCode
            setBusinessParam(GrayConstants.FILTER_PARAM_CITYCODE);
            //set curWorkCityCode
            setBusinessParam(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);
            //set attachmentKey
            setBusinessParam(GrayConstants.FILTER_PARAM_ATTACHMENT);

            return invoker.invoke(invocation);
        } finally {
            BusinessParamUtils.clear();
        }
    }

    private void setBusinessParam(String businessParamKey) {
        //用户自定义参数
        if (GrayConstants.FILTER_PARAM_ATTACHMENT.equals(businessParamKey)) {
            Set<String> keySet = RpcContext.getContext().getAttachments().keySet();
            if (CollectionUtils.isNotEmpty(keySet)) {
                for (String key : keySet) {
                    //不是灰度业务参数，跳过
                    if (key.startsWith(UserDefinedParamCachableProcessor.ATTACHMENT_PARAM_PREFIX)) {
                        UserDefinedParamCachableProcessor.getInstance().setAttachment(key, RpcContext.getContext().getAttachment(key));
                    }
                }
            }
        } else {
            String valueFromContext = RpcContext.getContext().getAttachment(businessParamKey);
            AbstractParamCachableProcessor<String> processor =
                    (AbstractParamCachableProcessor<String>) ParamProcessorFactory.getParamProcessByKey(businessParamKey);
            if (null != processor) {
                if (StringUtils.isNotEmpty(valueFromContext)) {
                    // 从RpcContext里获取 流量标识（例如：ucId） 并保存 provider端逻辑
                    processor.setValue(valueFromContext);
                }
            }
        }
    }

}
