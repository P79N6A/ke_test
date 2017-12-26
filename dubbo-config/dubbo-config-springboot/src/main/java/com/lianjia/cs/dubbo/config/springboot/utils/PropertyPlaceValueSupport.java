package com.lianjia.cs.dubbo.config.springboot.utils;

import org.springframework.core.env.PropertyResolver;

public class PropertyPlaceValueSupport {

    private String placeValueStart = "${";

    private String placeValueEnd = "}";

    private String valueSpliter = ":";

    public PropertyPlaceValueSupport() {
    }

    public PropertyPlaceValueSupport(String placeValueStart, String placeValueEnd, String valueSpliter) {
        this.placeValueStart = placeValueStart;
        this.placeValueEnd = placeValueEnd;
        this.valueSpliter = valueSpliter;
    }

    public void setPlaceValueStart(String placeValueStart) {
        this.placeValueStart = placeValueStart;
    }

    public void setPlaceValueEnd(String placeValueEnd) {
        this.placeValueEnd = placeValueEnd;
    }

    public void setValueSpliter(String valueSpliter) {
        this.valueSpliter = valueSpliter;
    }

    public String resolvePlaceValue(String val, PropertyResolver propertyResolver) {
        if (null == val) return val;
        int startIndex = val.indexOf(placeValueStart);
        int endIndex = val.indexOf(placeValueEnd);
        if (startIndex == -1 || endIndex == -1) return val;
        String placeHolder = val.substring(startIndex + placeValueStart.length(), endIndex);
        String defaultValue = null;
        int indexValueSplater = placeHolder.indexOf(valueSpliter);
        if (indexValueSplater > 0) {
            defaultValue = placeHolder.substring(indexValueSplater + valueSpliter.length());
        }
        String value = propertyResolver.getProperty(placeHolder);
        if (value == null) {
            value = defaultValue;
        }
        if (value == null) {
            throw new RuntimeException(val + " 没有默认值");
        }
        return val.replace(placeValueStart + placeHolder + placeValueEnd, value);
    }
}
