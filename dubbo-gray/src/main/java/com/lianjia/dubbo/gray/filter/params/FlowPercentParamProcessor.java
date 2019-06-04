package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

public class FlowPercentParamProcessor extends AbstractParamProcessor {

    /**
     *  MAX_LIMIT决定了圈定用户的精度，murmurHash保证了数据的散列性，只有在数据量足够的情况下保证数据的均匀性;
     */
    private int MAX_LIMIT = 10001;

    private static FlowPercentParamProcessor flowPercentParamProcess = new FlowPercentParamProcessor();

    public static FlowPercentParamProcessor getInstance() {
        return flowPercentParamProcess;
    }

    @Override
    protected boolean checkIsGrayFlow(String ucId, GrayRule _grayRule) {
        if (StringUtils.isEmpty(ucId)) {
            return false;
        }

        return  _grayRule.getGrayFlowPercent() <= 0 || grayFlowMapping(ucId,_grayRule.getGrayFlowPercent());
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void clear() {

    }

    public boolean grayFlowMapping(String ucId, int limit) {
        HashFunction hf = Hashing.murmur3_32();
        int hashCode = hf.newHasher().putString(ucId, Charsets.UTF_8).hash().asInt();
        long unsignedInt = hashCode & 0x0FFFFFFFFL;
        long map = unsignedInt % MAX_LIMIT;
        return map <= limit * 100;
    }
}
