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
public class CityCodeParamProcess extends AbstractParamProcess {

    public static final Logger log = LoggerFactory.getLogger(CityCodeParamProcess.class);

    private static final ThreadLocal<String> cityCodeCache = new ThreadLocal<String>();

    private static CityCodeParamProcess cityCodeParamProcess = new CityCodeParamProcess();
    public static CityCodeParamProcess getInstance(){
        return cityCodeParamProcess;
    }

    @Override
    public String getValue() {
        return cityCodeCache.get();
    }

    @Override
    public void setRealValue(String cityCode) {
        cityCodeCache.set(cityCode);
    }

    @Override
    public void clear() {
        cityCodeCache.remove();
    }

    @Override
    public boolean isGrayFlow(String cityCode, GrayRule _grayRule) {
        log.info("cityCode:{},cityCodeSet:{}", cityCode, _grayRule.getGrayCityCodeSet());
        if (StringUtils.isNotEmpty(cityCode) && _grayRule.getGrayCityCodeSet().contains(cityCode)) {
            return true;
        }
        return false;
    }
}
