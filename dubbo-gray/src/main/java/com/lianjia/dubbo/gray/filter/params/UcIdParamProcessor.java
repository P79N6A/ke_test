package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:21 AM
 * @Version: 1.0
 */
public class UcIdParamProcessor extends AbstractParamCachableProcessor<String> {

    public static final Logger log = LoggerFactory.getLogger(UcIdParamProcessor.class);

    private UcIdParamProcessor() {
    }

    private static UcIdParamProcessor ucIdParamProcess = new UcIdParamProcessor();

    public static UcIdParamProcessor getInstance() {
        return ucIdParamProcess;
    }


    @Override
    public boolean checkIsGrayFlow(String key,RuleInfo _ruleInfo) {

        String ucId = this.getGrayValue(key);

        log.debug("ucId:{},ucIdSet:{}", ucId, _ruleInfo.getGrayUcIdSet());
        if (StringUtils.isEmpty(ucId)) {
            return false;
        }

        if (CollectionUtils.isEmpty(_ruleInfo.getGrayUcIdSet())) {
            return false;
        }

        return _ruleInfo.getGrayUcIdSet().contains(ucId);
    }

    @Override
    public String getGrayValue(String key) {
        if (StringUtils.isBlank(key)) {
            key = GrayConstants.FILTER_PARAM_UCID;
        }
        String grayUcId = RpcContext.getContext().getAttachment(key);
        if (StringUtils.isEmpty(grayUcId)) {
            grayUcId = this.getValue();
        }
        return grayUcId;
    }
}
