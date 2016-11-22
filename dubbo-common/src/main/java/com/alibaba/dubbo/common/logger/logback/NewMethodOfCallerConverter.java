package com.alibaba.dubbo.common.logger.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by chengtianliang on 2016/11/22.
 */
public class NewMethodOfCallerConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent le) {
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > 2) {
            return cda[2].getMethodName();
        } else {
            return CallerData.NA;
        }
    }
}
