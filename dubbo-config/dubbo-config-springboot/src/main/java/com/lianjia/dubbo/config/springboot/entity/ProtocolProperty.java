package com.lianjia.dubbo.config.springboot.entity;

import com.lianjia.dubbo.config.springboot.Idable;

import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class ProtocolProperty implements Idable{

    private String id;

    private String host;

    private short port;

    private String name;

    private String threadpool;

    private Integer threads;

    private Integer iothreads;

    private Integer accepts;

    private Integer payload;

    private String codec;

    private String serialization;

    private String accesslog;

    private String path;

    private String transporter;

    private String server;

    private String client;

    private String dispatcher;

    private Integer queues;

    private String charset;

    private Integer buffer;

    private Integer heartbeat;

    private String telnet;

    private Boolean register;

    private String contextpath;

    private boolean defaultProtocol;

    private Map<String,Object> params;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThreadpool() {
        return threadpool;
    }

    public void setThreadpool(String threadpool) {
        this.threadpool = threadpool;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getIothreads() {
        return iothreads;
    }

    public void setIothreads(Integer iothreads) {
        this.iothreads = iothreads;
    }

    public Integer getAccepts() {
        return accepts;
    }

    public void setAccepts(Integer accepts) {
        this.accepts = accepts;
    }

    public Integer getPayload() {
        return payload;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getAccesslog() {
        return accesslog;
    }

    public void setAccesslog(String accesslog) {
        this.accesslog = accesslog;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Integer getQueues() {
        return queues;
    }

    public void setQueues(Integer queues) {
        this.queues = queues;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getBuffer() {
        return buffer;
    }

    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getTelnet() {
        return telnet;
    }

    public void setTelnet(String telnet) {
        this.telnet = telnet;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
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

    public boolean isDefaultProtocol() {
        return defaultProtocol;
    }

    public void setDefaultProtocol(boolean defaultProtocol) {
        this.defaultProtocol = defaultProtocol;
    }

    @Override
    public String toString() {
        return "ProtocolProperty{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", name='" + name + '\'' +
                ", threadpool='" + threadpool + '\'' +
                ", threads=" + threads +
                ", iothreads=" + iothreads +
                ", accepts=" + accepts +
                ", payload=" + payload +
                ", codec='" + codec + '\'' +
                ", serialization='" + serialization + '\'' +
                ", accesslog='" + accesslog + '\'' +
                ", path='" + path + '\'' +
                ", transporter='" + transporter + '\'' +
                ", server='" + server + '\'' +
                ", client='" + client + '\'' +
                ", dispatcher='" + dispatcher + '\'' +
                ", queues=" + queues +
                ", charset='" + charset + '\'' +
                ", buffer=" + buffer +
                ", heartbeat=" + heartbeat +
                ", telnet='" + telnet + '\'' +
                ", register=" + register +
                ", contextpath='" + contextpath + '\'' +
                ", defaultProtocol=" + defaultProtocol +
                ", params=" + params +
                '}';
    }
}
