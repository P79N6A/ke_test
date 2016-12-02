package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.config.*;
import com.lianjia.dubbo.config.springboot.entity.*;

/**
 * Created by chengtianliang on 2016/11/30.
 */
public class PropertyConfigCopyer {
    private PropertyConfigCopyer() {
    }

    public static void copyRegistryProperty2RegistryConfig(RegistryProperty registryProperty, RegistryConfig registryConfig) {
        registryConfig.setId(registryProperty.getId());
        registryConfig.setAddress(registryProperty.getAddress());
        registryConfig.setPort(registryProperty.getPort());
        registryConfig.setProtocol(registryProperty.getProtocol());
        registryConfig.setUsername(registryProperty.getUsername());
        registryConfig.setPassword(registryProperty.getPassword());
        registryConfig.setTransporter(registryProperty.getTransport());
        registryConfig.setServer(registryProperty.getServer());
        registryConfig.setClient(registryProperty.getClient());
        registryConfig.setCluster(registryProperty.getCluster());
        registryConfig.setGroup(registryProperty.getGroup());
        registryConfig.setVersion(registryProperty.getVersion());
        registryConfig.setTimeout(registryProperty.getTimeout());
        registryConfig.setSession(registryProperty.getSession());
        registryConfig.setFile(registryProperty.getFile());
        registryConfig.setCheck(registryProperty.getCheck());
        registryConfig.setDynamic(registryProperty.getDynamic());
        registryConfig.setRegister(registryProperty.getRegister());
        registryConfig.setSubscribe(registryProperty.getSubscribe());
        registryConfig.setParameters(registryProperty.getParameters());
//        registryConfig.setDefault(registryProperty.);
    }

    public static void copyProviderProperty2ProviderConfig(ProviderProperty providerProperty, ProviderConfig providerConfig) {
        providerConfig.setId(providerConfig.getId());
        providerConfig.setHost(providerProperty.getHost());
        providerConfig.setThreadpool(providerProperty.getThreadpool());
        providerConfig.setPayload(providerProperty.getPayload());
        providerConfig.setPath(providerProperty.getPath());
        providerConfig.setServer(providerProperty.getServer());
        providerConfig.setClient(providerProperty.getClient());
        providerConfig.setCodec(providerProperty.getCodec());
        providerConfig.setSerialization(providerProperty.getSerialization());
        providerConfig.setDefault(providerProperty.getDefault());
        providerConfig.setFilter(providerProperty.getFilter());
        providerConfig.setListener(providerProperty.getListener());
        providerConfig.setAccepts(providerProperty.getAccepts());
        providerConfig.setVersion(providerProperty.getVersion());
        providerConfig.setGroup(providerProperty.getGroup());
        providerConfig.setDelay(providerProperty.getDelay());
        providerConfig.setTimeout(providerProperty.getTimeout());
        providerConfig.setRetries(providerProperty.getRetries());
        providerConfig.setConnections(providerProperty.getConnections());
        providerConfig.setLoadbalance(providerProperty.getLoadbalance());
        providerConfig.setAsync(providerProperty.getAsync());
        providerConfig.setStub(providerProperty.getStub());
        providerConfig.setMock(providerProperty.getMock());
        providerConfig.setToken(providerProperty.getToken());
        String registry = providerProperty.getRegistry();
        if ("N/A".equalsIgnoreCase(registry)) {
            providerConfig.setRegister(Boolean.FALSE);
        }
//        providerConfig.setRegister("true".equals(providerProperty.getRegistry()));
        providerConfig.setDynamic(providerProperty.getDynamic());
        providerConfig.setAccesslog(providerProperty.getAccesslog());
        providerConfig.setOwner(providerProperty.getOwner());
        providerConfig.setDocument(providerProperty.getDocument());
        providerConfig.setWeight(providerProperty.getWeight());
        providerConfig.setExecutes(providerProperty.getExecutes());
        providerConfig.setActives(providerProperty.getActives());
        providerConfig.setProxy(providerProperty.getProxy());
        providerConfig.setCluster(providerProperty.getCluster());
        providerConfig.setDeprecated(providerProperty.getDeprecated());
        providerConfig.setQueues(providerProperty.getQueues());
        providerConfig.setCharset(providerProperty.getCharset());
        providerConfig.setBuffer(providerProperty.getBuffer());
        providerConfig.setIothreads(providerProperty.getIothreads());
        providerConfig.setTelnet(providerProperty.getTelnet());
        providerConfig.setContextpath(providerProperty.getContextpath());
        providerConfig.setLayer(providerProperty.getLayer());
    }

    public static void copyMonitoryProperty2MonitoryConfig(MonitorProperty monitorProperty, MonitorConfig monitorConfig) {
        monitorConfig.setProtocol(monitorProperty.getProtocol());
        monitorConfig.setAddress(monitorProperty.getAddress());
        monitorConfig.setUsername(monitorProperty.getUsername());
        monitorConfig.setDefault(monitorProperty.isDefaultMonitor());
        monitorConfig.setGroup(monitorProperty.getGroup());
        monitorConfig.setPassword(monitorProperty.getPassword());
        monitorConfig.setParameters(monitorProperty.getParameters());
        monitorConfig.setVersion(monitorProperty.getVersion());
        monitorConfig.setId(monitorProperty.getId());
    }

    public static void copyApplicationProperty2ApplicationConfig(ApplicationProperty applicationProperty, ApplicationConfig applicationConfig) {
        applicationConfig.setId(applicationProperty.getId() == null ? applicationProperty.getName() : applicationProperty.getId());
        applicationConfig.setVersion(applicationProperty.getVersion());
        applicationConfig.setArchitecture(applicationProperty.getArchitecture());
        applicationConfig.setCompiler(applicationProperty.getCompiler());
        applicationConfig.setDefault(applicationProperty.isDefaultApp());
        applicationConfig.setEnvironment(applicationProperty.getEnvironment());
        applicationConfig.setLogger(applicationProperty.getLogger());
        applicationConfig.setName(applicationProperty.getName());
        applicationConfig.setOrganization(applicationProperty.getOrganization());
        applicationConfig.setOwner(applicationProperty.getOwner());
    }

    public static void copyModuleProperty2ModuleConfig(ModuleProperty moduleProperty, ModuleConfig moduleConfig) {
        moduleConfig.setOwner(moduleProperty.getOwner());
        moduleConfig.setOrganization(moduleProperty.getOrganization());
        moduleConfig.setName(moduleProperty.getName());
        moduleConfig.setDefault(moduleProperty.isDefaultModule());
        moduleConfig.setVersion(moduleProperty.getVersion());
        moduleConfig.setId(moduleProperty.getId() == null ? moduleProperty.getName() : moduleProperty.getId());
    }

    public static void copyProtocolProperty2ProtocolConfig(ProtocolProperty protocolProperty, ProtocolConfig protocolConfig) {
        protocolConfig.setId(protocolProperty.getId() == null ? protocolProperty.getName() : protocolProperty.getId());
        protocolConfig.setName(protocolProperty.getName());
        protocolConfig.setPort(protocolProperty.getPort());
        protocolConfig.setHost(protocolProperty.getHost());
        protocolConfig.setThreadpool(protocolProperty.getThreadpool());
        protocolConfig.setThreads(protocolProperty.getThreads());
        protocolConfig.setIothreads(protocolProperty.getIothreads());
        protocolConfig.setAccepts(protocolProperty.getAccepts());
        protocolConfig.setPayload(protocolProperty.getPayload());
        protocolConfig.setCodec(protocolProperty.getCodec());
        protocolConfig.setSerialization(protocolProperty.getSerialization());
        protocolConfig.setAccesslog(protocolProperty.getAccesslog());
        protocolConfig.setPath(protocolProperty.getPath());
        protocolConfig.setTransporter(protocolProperty.getTransporter());
        protocolConfig.setServer(protocolProperty.getServer());
        protocolConfig.setClient(protocolProperty.getClient());
        protocolConfig.setDispatcher(protocolProperty.getDispatcher());
        protocolConfig.setQueues(protocolProperty.getQueues());
        protocolConfig.setCharset(protocolProperty.getCharset());
        protocolConfig.setBuffer(protocolProperty.getBuffer());
        protocolConfig.setHeartbeat(protocolProperty.getHeartbeat());
        protocolConfig.setTelnet(protocolProperty.getTelnet());
        protocolConfig.setRegister(protocolProperty.getRegister());
        protocolConfig.setContextpath(protocolProperty.getContextpath());
    }

    public static void copyConsumeProperty2ConsumeConfig(ConsumerProperty consumerProperty, ConsumerConfig consumerConfig) {
        consumerConfig.setTimeout(consumerProperty.getTimeout());
        consumerConfig.setRetries(consumerProperty.getRetries());
        consumerConfig.setLoadbalance(consumerProperty.getLoadbalance());
        consumerConfig.setAsync(consumerProperty.getAsync());
        consumerConfig.setConnections(consumerProperty.getConnections());
        consumerConfig.setGeneric(consumerProperty.getGeneric());
        consumerConfig.setCheck(consumerProperty.getCheck());
        consumerConfig.setProxy(consumerProperty.getProxy());
        consumerConfig.setOwner(consumerProperty.getOwner());
        consumerConfig.setActives(consumerProperty.getActives());
        consumerConfig.setCluster(consumerProperty.getCluster());
        consumerConfig.setFilter(consumerProperty.getFilter());
        consumerConfig.setListener(consumerProperty.getListener());
        consumerConfig.setLayer(consumerProperty.getLayer());
        consumerConfig.setInit(consumerProperty.getInit());
        consumerConfig.setCache(consumerProperty.getCache());
        consumerConfig.setValidation(consumerProperty.getCache());
        consumerConfig.setId(consumerProperty.getId());
//        consumerConfig.setReg
    }
}
