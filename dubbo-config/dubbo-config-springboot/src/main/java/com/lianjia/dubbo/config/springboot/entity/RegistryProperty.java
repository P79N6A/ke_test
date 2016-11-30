package com.lianjia.dubbo.config.springboot.entity;

import com.lianjia.dubbo.config.springboot.Idable;

import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class RegistryProperty implements Idable{

    private String id;

    private String address;

    private String protocol;

    private Integer port;

    private String username;

    private String password;

    private String transport;

    private Integer timeout;

    private Integer session;

    private String file;

    private Integer wait;

    private Boolean check;

    private Boolean register;

    private Boolean subscribe;

    private Boolean dynamic;

    private Map<String,Object> params;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getSession() {
        return session;
    }

    public void setSession(Integer session) {
        this.session = session;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getWait() {
        return wait;
    }

    public void setWait(Integer wait) {
        this.wait = wait;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
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

    @Override
    public String toString() {
        return "RegistryProperty{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", transport='" + transport + '\'' +
                ", timeout=" + timeout +
                ", session=" + session +
                ", file='" + file + '\'' +
                ", wait=" + wait +
                ", check=" + check +
                ", register=" + register +
                ", subscribe=" + subscribe +
                ", dynamic=" + dynamic +
                ", params=" + params +
                '}';
    }
}
