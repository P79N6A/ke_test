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

    private String username;

    private String password;

    private String group;

    private String version;

    // 自定义参数
    private Map<String, String> parameters;

    // 是否为缺省
    private boolean defaultMonitor;

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

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
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
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", group='" + group + '\'' +
                ", version='" + version + '\'' +
                ", parameters=" + parameters +
                ", defaultMonitor=" + defaultMonitor +
                '}';
    }
}
