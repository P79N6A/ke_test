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
import com.lianjia.dubbo.gray.filter.GrayConstants;
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

    public static final String DUBBO_GRAY_KEY = "dubboGrayJson";

    @ApolloConfig
    private Config apolloConfig;

    public String getDubboGrayJson() {
        String temp = apolloConfig.getProperty(DUBBO_GRAY_KEY, GrayConstants.EMPTY_STR);
        logger.info("dubboGrayJson{}", temp);
        return temp;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //load config info to  GrayRulesCache
        updateGrayRulesCache(getDubboGrayJson());

        //add change event
        apolloConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                //update injected value of batch if it is changed in Apollo
                if (changeEvent.isChanged(DUBBO_GRAY_KEY)) {
                    updateGrayRulesCache(getDubboGrayJson());
                }
            }
        });
    }

    private void updateGrayRulesCache(String dubboGray) {
        if (StringUtils.isNotEmpty(dubboGray)) {
            List<GrayRule> grayRuleList = JSONArray.parseArray(dubboGray, GrayRule.class);
            if (CollectionUtils.isNotEmpty(grayRuleList)) {
                GrayRulesCache.getInstance().updateGrayRules(grayRuleList);
            }
        }
    }


}
