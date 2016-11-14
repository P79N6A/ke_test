package com.alibaba.dubbo.common.logger.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by chengtianliang on 14/12/6.
 */
public class NewPatterLayout extends PatternLayout {
    @Override
    public String doLayout(ILoggingEvent event) {
        String layout = super.doLayout(event);
        layout = buildLayout(layout);
        return layout;
    }

    private String buildLayout(String baseLayout) {
        Map<String, Object> map = LoggerFactory.getDatas();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey() == null) continue;
            Object value = entry.getValue();
            baseLayout = StringUtils.replace(baseLayout, "$" + entry.getKey(), value == null ? "null" : value.toString());
        }
        return baseLayout;
    }
}
