package com.lianjia.cs.dubbo.config.springboot.entity;

import com.lianjia.cs.dubbo.config.springboot.Idable;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class ProviderProperty implements Idable{

    private String id;

    private String protocol;

    private String host;

    private Integer threads;

    private Integer payload;

    private String path;

    private String server;

    private String client;

    private String codec;

    private String serialization;

    private Boolean isDefault;

    private String filter;

    private String listener;

    private String threadpool;

    private Integer accepts;

    private String version;

    private String group;

    private Integer delay;

    private Integer timeout;

    private Integer retries;

    private Integer connections;

    private String loadbalance;

    private Boolean async;

    private Boolean stub;

    private Boolean mock;

    private Boolean token;

    private String registry;

    private Boolean dynamic;

    private String accesslog;

    private String owner;

    private String document;

    private Integer weight;

    private Integer executes;

    private Integer actives;

    private String proxy;

    private String cluster;

    private Boolean deprecated;

    private Integer queues;

    private String charset;

    private Integer buffer;

    private Integer iothreads;

    private String telnet;

    private String contextpath;

    private String layer;

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getAccesslog() {
        return accesslog;
    }

    public void setAccesslog(String accesslog) {
        this.accesslog = accesslog;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getExecutes() {
        return executes;
    }

    public void setExecutes(Integer executes) {
        this.executes = executes;
    }

    public Integer getActives() {
        return actives;
    }

    public void setActives(Integer actives) {
        this.actives = actives;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
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

    public Integer getIothreads() {
        return iothreads;
    }

    public void setIothreads(Integer iothreads) {
        this.iothreads = iothreads;
    }

    public String getTelnet() {
        return telnet;
    }

    public void setTelnet(String telnet) {
        this.telnet = telnet;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getPayload() {
        return payload;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getThreadpool() {
        return threadpool;
    }

    public void setThreadpool(String threadpool) {
        this.threadpool = threadpool;
    }

    public Integer getAccepts() {
        return accepts;
    }

    public void setAccepts(Integer accepts) {
        this.accepts = accepts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

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

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
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

    public Boolean getStub() {
        return stub;
    }

    public void setStub(Boolean stub) {
        this.stub = stub;
    }

    public Boolean getMock() {
        return mock;
    }

    public void setMock(Boolean mock) {
        this.mock = mock;
    }

    public Boolean getToken() {
        return token;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "ProviderProperty{" +
                "id='" + id + '\'' +
                ", protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", threads=" + threads +
                ", payload=" + payload +
                ", path='" + path + '\'' +
                ", server='" + server + '\'' +
                ", client='" + client + '\'' +
                ", codec='" + codec + '\'' +
                ", serialization='" + serialization + '\'' +
                ", isDefaultApp=" + isDefault +
                ", filter='" + filter + '\'' +
                ", listener='" + listener + '\'' +
                ", threadpool='" + threadpool + '\'' +
                ", accepts=" + accepts +
                ", version='" + version + '\'' +
                ", group='" + group + '\'' +
                ", delay=" + delay +
                ", timeout=" + timeout +
                ", retries=" + retries +
                ", connections=" + connections +
                ", loadbalance='" + loadbalance + '\'' +
                ", async=" + async +
                ", stub=" + stub +
                ", mock=" + mock +
                ", token=" + token +
                ", registry='" + registry + '\'' +
                ", dynamic=" + dynamic +
                ", accesslog='" + accesslog + '\'' +
                ", owner='" + owner + '\'' +
                ", document='" + document + '\'' +
                ", weight=" + weight +
                ", executes=" + executes +
                ", actives=" + actives +
                ", proxy='" + proxy + '\'' +
                ", cluster='" + cluster + '\'' +
                ", deprecated=" + deprecated +
                ", queues=" + queues +
                ", charset='" + charset + '\'' +
                ", buffer=" + buffer +
                ", iothreads=" + iothreads +
                ", telnet='" + telnet + '\'' +
                ", contextpath='" + contextpath + '\'' +
                ", layer='" + layer + '\'' +
                '}';
    }
}
