package com.lianjia.dubbo.gray.rule.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Set;

/**
 * @author liupinghe
 */
public class GrayRule {

    /**
     * 灰度应用
     */
    private String application;

    /**
     * 灰度机器IP
     */
    private String serverIp;

    /**
     * 灰度应用端口
     */
    private int serverPort;
    /**
     * 灰度是否开启
     */
    private boolean isOpen;

    /**
     * 灰度账号
     */
    private Set<String> grayUcIdSet;

    /**
     * 当前城市
     */
    private Set<String> grayCityCodeSet;

    /**
     * 当前作业城市
     */
    private Set<String> grayCurWorkCityCodeSet;

    /**
     * 灰度流量百分比
     */
    private int grayFlowPercent;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Set<String> getGrayUcIdSet() {
        return grayUcIdSet;
    }

    public void setGrayUcIdSet(Set<String> grayUcIdSet) {
        this.grayUcIdSet = grayUcIdSet;
    }

    public Set<String> getGrayCityCodeSet() {
        return grayCityCodeSet;
    }

    public void setGrayCityCodeSet(Set<String> grayCityCodeSet) {
        this.grayCityCodeSet = grayCityCodeSet;
    }

    public Set<String> getGrayCurWorkCityCodeSet() {
        return grayCurWorkCityCodeSet;
    }

    public void setGrayCurWorkCityCodeSet(Set<String> grayCurWorkCityCodeSet) {
        this.grayCurWorkCityCodeSet = grayCurWorkCityCodeSet;
    }

    public int getGrayFlowPercent() {
        return grayFlowPercent;
    }

    public void setGrayFlowPercent(int grayFlowPercent) {
        this.grayFlowPercent = grayFlowPercent;
    }

    @JSONField(serialize = false)
    public String getKey() {

        return getServerIp() + "_" + getServerPort();
    }
}
