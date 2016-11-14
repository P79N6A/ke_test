package com.alibaba.dubbo.common.logger.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * Created by chengtianliang on 14/12/6.
 */
public class PatternEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {
    @Override
    public void start() {
        NewPatterLayout patternLayout = new NewPatterLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(true);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }
}
