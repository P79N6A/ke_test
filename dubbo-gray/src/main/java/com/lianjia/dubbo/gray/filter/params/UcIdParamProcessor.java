package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:21 AM
 * @Version: 1.0
 */
public class UcIdParamProcessor extends AbstractParamProcessor {

    public static final Logger log = LoggerFactory.getLogger(UcIdParamProcessor.class);

    private static final ThreadLocal<String> ucIdCache = new ThreadLocal<String>();

    private UcIdParamProcessor() {
    }

    private static UcIdParamProcessor ucIdParamProcess = new UcIdParamProcessor();

    public static UcIdParamProcessor getInstance() {
        return ucIdParamProcess;
    }

    @Override
    public String getValue() {
        return ucIdCache.get();
    }

    @Override
    public void setValue(String ucId) {
        ucIdCache.set(ucId);
    }

    @Override
    public void clear() {
        ucIdCache.remove();
    }

    @Override
    public boolean checkIsGrayFlow(String ucId, GrayRule _grayRule) {

        log.info("ucId:{},ucIdSet:{}", ucId, _grayRule.getGrayUcIdSet());
        if (StringUtils.isEmpty(ucId)) {
            return false;
        }

        return _grayRule.getGrayUcIdSet().contains(ucId);
    }
}
