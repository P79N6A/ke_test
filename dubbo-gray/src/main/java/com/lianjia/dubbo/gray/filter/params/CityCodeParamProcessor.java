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
        log.debug("cityCode:{},cityCodeMap:{}", cityCode, _grayRule.getGrayCityCodeMap());
        if (StringUtils.isEmpty(cityCode)) {
            return false;
        }

        if (_grayRule == null || _grayRule.getGrayCityCodeMap() == null
                || _grayRule.getGrayCityCodeMap().size() == 0){
            return false;
        }

        //不包含当前城市，直接返回false
        if (!_grayRule.getGrayCityCodeMap().containsKey(cityCode)){
            return false;
        }

        Integer limit = _grayRule.getGrayCityCodeMap().get(cityCode);
        //未开启百分比
        if (limit <= 0) {
            return true;
        }

        return CityFlowPercentUtil.grayFlowMapping(
                ParamProcessorFactory.getParamProcessByKey(GrayConstants.FILTER_PARAM_UCID).getGrayValue(), limit);

    }

    @Override
    public String getGrayValue() {
        String grayCityCode = RpcContext.getContext().getAttachment(GrayConstants.FILTER_PARAM_CITYCODE);
        if (StringUtils.isEmpty(grayCityCode)) {
            grayCityCode = this.getValue();
        }
        return grayCityCode;
    }
}
