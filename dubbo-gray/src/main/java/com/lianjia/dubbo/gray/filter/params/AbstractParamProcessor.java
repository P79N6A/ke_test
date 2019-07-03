package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:17 AM
 * @Version: 1.0
 */
public abstract class AbstractParamProcessor implements IParamProcessor {

    public static final Logger logger = LoggerFactory.getLogger(AbstractParamProcessor.class);

    protected abstract boolean checkIsGrayFlow(String key, RuleInfo _ruleInfo);

    @Override
    public boolean isGrayFlow(String key, RuleInfo _ruleInfo) {
        return checkIsGrayFlow(key, _ruleInfo);
    }

    /**
     * 由于 ketrace 在传递过程中，会将 String 转化成 Set，所以在获取需要解析
     *
     * @param value
     * @return
     */
    protected String getFilterParamValue(String value) {
        //FIXME：解析格式问题,比较单一
        if (value == null || GrayConstants.EMPTY_STR.equals(value)) {
            return GrayConstants.EMPTY_STR;
        }
        return value.replace(GrayConstants.BRACKET_LEFT, GrayConstants.EMPTY_STR)
                .replaceAll(GrayConstants.BRACKET_RIGHT, GrayConstants.EMPTY_STR)
                .replaceAll(GrayConstants.DOUBLE_QUOTES, GrayConstants.EMPTY_STR)
                .replaceAll(GrayConstants.SINGLE_QUOTES, GrayConstants.EMPTY_STR);
    }

}
