package com.lianjia.dubbo.config.springboot.entity;

import com.lianjia.dubbo.config.springboot.Idable;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class ConsumerProperty implements Idable{

    private String id;

    private Integer timeout;

    private Integer retries;

    private String loadbalance;

    private Boolean async;

    private Integer connections;

    private boolean defaultConsumer;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefaultConsumer() {
        return defaultConsumer;
    }

    public void setDefaultConsumer(boolean defaultConsumer) {
        this.defaultConsumer = defaultConsumer;
    }

    @Override
    public String toString() {
        return "ConsumerProperty{" +
                "timeout=" + timeout +
                ", retries=" + retries +
                ", loadbalance='" + loadbalance + '\'' +
                ", async=" + async +
                ", connections=" + connections +
                '}';
    }
}
