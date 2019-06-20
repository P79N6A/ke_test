package com.lianjia.dubbo.gray.cluster.loadbalance;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.lianjia.dubbo.gray.common.GrayConstants;
import com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@EnableApolloConfig
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DubboGrayApolloConfig implements InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(DubboGrayApolloConfig.class);


    @ApolloConfig(GrayConstants.DUBBO_GRAY_NAMESPACE)
    private Config apolloConfig;

    public String getDubboGrayJson() {
        if (null == apolloConfig) {
            return null;
        }

        String temp = apolloConfig.getProperty(GrayConstants.DUBBO_GRAY_KEY, GrayConstants.EMPTY_STR);
        logger.info("dubboGrayJson:{}", temp);
        return temp;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //load config info to  GrayRulesCache
        updateGrayRulesCache(getDubboGrayJson());
        if (null == apolloConfig) {
            return;
        }
        //add change event
        apolloConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                //update injected value of batch if it is changed in Apollo
                if (changeEvent.isChanged(GrayConstants.DUBBO_GRAY_KEY)) {
                    updateGrayRulesCache(getDubboGrayJson());
                }
            }
        });
    }

    private void updateGrayRulesCache(String dubboGray) {
        if (StringUtils.isNotEmpty(dubboGray)) {
            List<GrayRule> grayRuleList = null;
            try {
                grayRuleList = JSONArray.parseArray(dubboGray, GrayRule.class);
            } catch (Exception e) {
                logger.error("gray config is error:{}", e);
            }
            if (CollectionUtils.isNotEmpty(grayRuleList)) {
                GrayRulesCache.updateGrayRules(grayRuleList);
            }
        }
    }


}
