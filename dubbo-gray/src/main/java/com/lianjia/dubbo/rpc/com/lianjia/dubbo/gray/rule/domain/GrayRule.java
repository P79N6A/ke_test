package com.lianjia.dubbo.rpc.com.lianjia.dubbo.gray.rule.domain;

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

    @JSONField(serialize = false)
    public String getKey() {

        return getServerIp() + "_" + getServerPort();
    }
}
