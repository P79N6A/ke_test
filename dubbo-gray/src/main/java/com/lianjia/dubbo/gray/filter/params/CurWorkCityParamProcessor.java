package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:22 AM
 * @Version: 1.0
 */
public class CurWorkCityParamProcessor extends AbstractParamCachableProcessor {

    public static final Logger log = LoggerFactory.getLogger(CurWorkCityParamProcessor.class);

    private CurWorkCityParamProcessor() {
    }

    private static CurWorkCityParamProcessor curWorkCityParamProcess = new CurWorkCityParamProcessor();

    public static CurWorkCityParamProcessor getInstance() {
        return curWorkCityParamProcess;
    }

    @Override
    public boolean checkIsGrayFlow(String curWorkCityCode, GrayRule _grayRule) {
        log.debug("curWorkCityCode:{},curWorkCityCodeSet:{}", curWorkCityCode, _grayRule.getGrayCurWorkCityCodeSet());

        if (StringUtils.isEmpty(curWorkCityCode)) {
            return false;
        }
        return _grayRule.getGrayCurWorkCityCodeSet().contains(curWorkCityCode);
    }
}
