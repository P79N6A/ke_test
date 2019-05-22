package com.lianjia.dubbo.rpc.cluster.loadbalance;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.domain.GrayRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableApolloConfig
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DubboGrayApolloConfig {

    public static final Logger logger = LoggerFactory.getLogger(DubboGrayApolloConfig.class);

    public static final String dubboGrayKey = "dubboGrayJson";

    @ApolloConfig
    private Config defaultConfig;

    private String dubboGrayJson;


    public String getDubboGrayJson() {
        return dubboGrayJson;
    }

    @Value("${dubboGrayJson}")
    public void setDubboGrayJson(String dubboGrayJson) {
        this.dubboGrayJson = dubboGrayJson;
        // init update
        updateGrayRulesCache(dubboGrayJson);
    }

    @ApolloConfigChangeListener
    private void someOnChange(ConfigChangeEvent changeEvent) {
        //update injected value of batch if it is changed in Apollo
        if (changeEvent.isChanged(dubboGrayKey)) {
            dubboGrayJson = defaultConfig.getProperty(dubboGrayKey, "");
            logger.info("dubboGrayJson{}", dubboGrayJson);
            updateGrayRulesCache(dubboGrayJson);

        }
    }

    private void updateGrayRulesCache(String dubboGray) {

        if (StringUtils.isNotEmpty(dubboGray)) {
            List<GrayRule> grayRuleList = JSONArray.parseArray(dubboGray, GrayRule.class);
            for (GrayRule grayRule : grayRuleList) {
                GrayRulesCache.getInstance().getGrayRuleHashMap().put(grayRule.getKey(), grayRule);
            }
        }
    }


}
