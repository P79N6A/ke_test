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
public class CityCodeParamProcessor extends AbstractParamCachableProcessor {

    public static final Logger log = LoggerFactory.getLogger(CityCodeParamProcessor.class);

    private CityCodeParamProcessor() {
    }

    private static CityCodeParamProcessor cityCodeParamProcess = new CityCodeParamProcessor();

    public static CityCodeParamProcessor getInstance() {
        return cityCodeParamProcess;
    }

    @Override
    public boolean checkIsGrayFlow(String cityCode, GrayRule _grayRule) {
        log.debug("cityCode:{},cityCodeSet:{}", cityCode, _grayRule.getGrayCityCodeSet());
        if (StringUtils.isEmpty(cityCode)) {
            return false;
        }
        return _grayRule.getGrayCityCodeSet().contains(cityCode);
    }
}
