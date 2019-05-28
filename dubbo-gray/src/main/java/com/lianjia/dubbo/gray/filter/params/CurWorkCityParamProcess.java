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
public class CurWorkCityParamProcess extends AbstractParamProcess {

    public static final Logger log = LoggerFactory.getLogger(CurWorkCityParamProcess.class);

    private static final ThreadLocal<String> curWorkCityCodeCache = new ThreadLocal<>();

    private CurWorkCityParamProcess() {

    }

    private static CurWorkCityParamProcess curWorkCityParamProcess = new CurWorkCityParamProcess();
    public static CurWorkCityParamProcess getInstance(){
        return curWorkCityParamProcess;
    }

    @Override
    public String getValue() {
        return curWorkCityCodeCache.get();
    }

    @Override
    public void setRealValue(String curWorkCityCode){
        curWorkCityCodeCache.set(curWorkCityCode);
    }


    @Override
    public void clear() {
        curWorkCityCodeCache.remove();
    }

    @Override
    public boolean isGrayFlow(String curWorkCityCode, GrayRule _grayRule) {
        log.info("curWorkCityCode:{},curWorkCityCodeSet:{}", curWorkCityCode, _grayRule.getGrayCurWorkCityCodeSet());
        if (StringUtils.isNotEmpty(curWorkCityCode) && _grayRule.getGrayCurWorkCityCodeSet().contains(curWorkCityCode)) {
            return true;
        }
        return false;
    }
}
