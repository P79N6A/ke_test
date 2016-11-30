package com.lianjia.dubbo.config.springboot.entity;

import com.lianjia.dubbo.config.springboot.Idable;

import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class MonitorProperty implements Idable{

    private String id;

    private String protocol;

    private String address;

    private boolean defaultMonitor;

    private Map<String,Object> params;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefaultMonitor() {
        return defaultMonitor;
    }

    public void setDefaultMonitor(boolean defaultMonitor) {
        this.defaultMonitor = defaultMonitor;
    }

    @Override
    public String toString() {
        return "MonitorProperty{" +
                "id='" + id + '\'' +
                ", protocol='" + protocol + '\'' +
                ", address='" + address + '\'' +
                ", defaultMonitor=" + defaultMonitor +
                ", params=" + params +
                '}';
    }
}
