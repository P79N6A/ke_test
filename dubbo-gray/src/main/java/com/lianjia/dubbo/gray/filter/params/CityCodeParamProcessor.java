package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.filter.CityFlowPercentUtil;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

/**
 * @Description: 类信息描述
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/5/28 11:22 AM
 * @Version: 1.0
 */
public class CityCodeParamProcessor extends AbstractParamCachableProcessor<String> {

    public static final Logger log = LoggerFactory.getLogger(CityCodeParamProcessor.class);

    private CityCodeParamProcessor() {
    }

    private static CityCodeParamProcessor cityCodeParamProcess = new CityCodeParamProcessor();

    public static CityCodeParamProcessor getInstance() {
        return cityCodeParamProcess;
    }

    @Override
    public boolean checkIsGrayFlow(String key, RuleInfo _ruleInfo) {
        String cityCode = this.getGrayValue(key);
        if (StringUtils.isEmpty(cityCode)) {
            return false;
        }

        if (_ruleInfo == null || _ruleInfo.getGrayCityCodeMap() == null
                || _ruleInfo.getGrayCityCodeMap().size() == 0) {
            return false;
        }

        log.debug("cityCode:{},cityCodeMap:{}", cityCode, _ruleInfo.getGrayCityCodeMap());

        //不包含当前城市，直接返回false
        if (!_ruleInfo.getGrayCityCodeMap().containsKey(cityCode)) {
            return false;
        }

        Integer limit = _ruleInfo.getGrayCityCodeMap().get(cityCode);
        //未开启百分比
        if (limit <= 0) {
            return true;
        }

        return CityFlowPercentUtil.grayFlowMapping(
                this.getGrayValue(GrayConstants.FILTER_PARAM_CITYCODE), limit);

    }

    @Override
    public String getGrayValue(String key) {
        if (StringUtils.isBlank(key)) {
            key = GrayConstants.FILTER_PARAM_CITYCODE;
        }
        String grayCityCode = RpcContext.getContext().getAttachment(key);
        if (StringUtils.isEmpty(grayCityCode)) {
            grayCityCode = this.getValue();
        }
        return grayCityCode;
    }
}
