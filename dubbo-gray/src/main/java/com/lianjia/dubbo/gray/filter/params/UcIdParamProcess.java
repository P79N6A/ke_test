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
public class UcIdParamProcess extends AbstractParamProcess {

    public static final Logger log = LoggerFactory.getLogger(UcIdParamProcess.class);

    private static final ThreadLocal<String> ucIdCache = new ThreadLocal<String>();

    private UcIdParamProcess() {

    }

    private static UcIdParamProcess ucIdParamProcess = new UcIdParamProcess();
    public static UcIdParamProcess getInstance(){
        return ucIdParamProcess;
    }

    @Override
    public String getValue() {
        return ucIdCache.get();
    }

    @Override
    public void setRealValue(String ucId) {
        ucIdCache.set(ucId);
    }

    @Override
    public void clear() {
        ucIdCache.remove();
    }

    @Override
    public boolean isGrayFlow(String ucId,GrayRule _grayRule) {
        log.info("ucId:{},ucIdSet:{}", ucId, _grayRule.getGrayUcIdSet());
        if (StringUtils.isNotEmpty(ucId) && _grayRule.getGrayUcIdSet().contains(ucId)) {
            return true;
        }
        return false;
    }
}
