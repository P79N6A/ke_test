package com.lianjia.dubbo.gray.rule.domain;

import java.util.Map;

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
    private Map<String, RuleInfo> grayServerIpMap;

    /**
     * 灰度应用端口
     */
    private int serverPort;
    /**
     * 灰度是否开启
     */
    private boolean isOpen;


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


    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Map<String, RuleInfo> getGrayServerIpMap() {
        return grayServerIpMap;
    }

    public GrayRule setGrayServerIpMap(Map<String, RuleInfo> grayServerIpMap) {
        this.grayServerIpMap = grayServerIpMap;
        return this;
    }
}
