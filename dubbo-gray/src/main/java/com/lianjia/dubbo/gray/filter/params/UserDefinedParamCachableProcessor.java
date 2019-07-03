package com.lianjia.dubbo.gray.filter.params;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 用户自定义参数
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/7/3 12:19 PM
 * @Version: 1.0
 */
public class UserDefinedParamCachableProcessor extends AbstractParamCachableProcessor<Map<String, String>> {

    public static final String ATTACHMENT_PARAM_PREFIX = "dubbo_gray_attachment_";

    public static final Logger log = LoggerFactory.getLogger(UserDefinedParamCachableProcessor.class);

    private static UserDefinedParamCachableProcessor userDefinedParamCachableProcessor = new UserDefinedParamCachableProcessor();

    private UserDefinedParamCachableProcessor() {
    }

    public static UserDefinedParamCachableProcessor getInstance() {
        return userDefinedParamCachableProcessor;
    }

    @Override
    protected boolean checkIsGrayFlow(String key, RuleInfo _ruleInfo) {
        String attachmentKey = getGrayValue(key);
        if (StringUtils.isBlank(attachmentKey)) {
            return false;
        }

        if (_ruleInfo == null || _ruleInfo.getAttachmentsMap() == null
                || _ruleInfo.getGrayCityCodeMap().size() == 0) {
            return false;
        }
        log.debug("attachmentKey:{}, ");

        String value = _ruleInfo.getAttachmentsMap().get(attachmentKey);
        if (null == value) {
            return false;
        }

        return value.contains(getGrayValue(attachmentKey));
    }

    @Override
    public String getGrayValue(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return RpcContext.getContext().getAttachment(key);
    }

    public void setAttachment(String key, String value) {
        //init
        if (null == getValue()) {
            setValue(new ConcurrentHashMap<String, String>());
        }
        getValue().put(generageKey(key), value);
    }

    public String getAttachment(String key) {
        return getValue().get(generageKey(key));
    }

    public String generageKey(String key) {
        if (StringUtils.isNotEmpty(key) && key.startsWith(ATTACHMENT_PARAM_PREFIX)){
            return key;
        }
        return ATTACHMENT_PARAM_PREFIX + key;
    }
}
