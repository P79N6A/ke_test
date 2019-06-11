package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.gray.filter.CityFlowPercentUtil;
import com.lianjia.dubbo.gray.filter.GrayConstants;
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
        log.debug("curWorkCityCode:{},curWorkCityCodeMap:{}", curWorkCityCode, _grayRule.getGrayCurWorkCityCodeMap());

        if (StringUtils.isEmpty(curWorkCityCode)) {
            return false;
        }

        //当前作业城市配置为空
        if (_grayRule == null || _grayRule.getGrayCurWorkCityCodeMap() == null
                || _grayRule.getGrayCurWorkCityCodeMap().size() == 0) {
            return false;
        }

        //不包含当前城市
        if (!_grayRule.getGrayCurWorkCityCodeMap().containsKey(curWorkCityCode)) {
            return false;
        }

        int limit = _grayRule.getGrayCurWorkCityCodeMap().get(curWorkCityCode);
        //未开启百分比
        if (limit <= 0) {
            return true;
        }

        return CityFlowPercentUtil.grayFlowMapping(
                ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID).getGrayValue(), limit);
    }

    @Override
    public String getGrayValue() {
        String curWorkCityCode = RpcContext.getContext().getAttachment(GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE);
        if (StringUtils.isEmpty(curWorkCityCode)) {
            curWorkCityCode = this.getValue();
        }
        return curWorkCityCode;
    }
}
