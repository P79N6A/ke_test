package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:21 AM
 * @Version: 1.0
 */
public class UcIdParamProcessor extends AbstractParamCachableProcessor {

    public static final Logger log = LoggerFactory.getLogger(UcIdParamProcessor.class);

    private UcIdParamProcessor() {
    }

    private static UcIdParamProcessor ucIdParamProcess = new UcIdParamProcessor();

    public static UcIdParamProcessor getInstance() {
        return ucIdParamProcess;
    }


    @Override
    public boolean checkIsGrayFlow(String ucId, GrayRule _grayRule) {

        log.debug("ucId:{},ucIdSet:{}", ucId, _grayRule.getGrayUcIdSet());
        if (StringUtils.isEmpty(ucId)) {
            return false;
        }

        if (CollectionUtils.isEmpty(_grayRule.getGrayUcIdSet())){
            return false;
        }

        return _grayRule.getGrayUcIdSet().contains(ucId);
    }

    @Override
    public String getGrayValue() {
        String grayUcId = RpcContext.getContext().getAttachment(GrayConstants.FILTER_PARAM_UCID);
        if (StringUtils.isEmpty(grayUcId)) {
            grayUcId = this.getValue();
        }
        return grayUcId;
    }
}
