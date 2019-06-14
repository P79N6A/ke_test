package com.lianjia.dubbo.gray.rule.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
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
     * 灰度机器IP组
     */
    private Set<String> serverIpSet;

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
    private Map<String, Integer> grayCityCodeMap;

    /**
     * 当前作业城市
     */
    private Map<String, Integer> grayCurWorkCityCodeMap;

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

    public Set<String> getServerIpSet() {
        return serverIpSet;
    }

    public GrayRule setServerIpSet(Set<String> serverIpSet) {
        this.serverIpSet = serverIpSet;
        return this;
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

    public Map<String, Integer> getGrayCityCodeMap() {
        return grayCityCodeMap;
    }

    public GrayRule setGrayCityCodeMap(Map<String, Integer> grayCityCodeMap) {
        this.grayCityCodeMap = grayCityCodeMap;
        return this;
    }

    public Map<String, Integer> getGrayCurWorkCityCodeMap() {
        return grayCurWorkCityCodeMap;
    }

    public GrayRule setGrayCurWorkCityCodeMap(Map<String, Integer> grayCurWorkCityCodeMap) {
        this.grayCurWorkCityCodeMap = grayCurWorkCityCodeMap;
        return this;
    }

}
