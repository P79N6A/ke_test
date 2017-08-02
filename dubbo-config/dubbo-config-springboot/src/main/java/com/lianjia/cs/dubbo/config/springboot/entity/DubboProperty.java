package com.lianjia.cs.dubbo.config.springboot.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chengtianliang on 2016/11/29.
 */
//@Configuration
//@Component("dubboPropertyConfig")
@ConfigurationProperties(prefix = "dubbo")
public class DubboProperty {

    private String scanPackage;

    @NestedConfigurationProperty
    private List<ApplicationProperty> applications;

    @NestedConfigurationProperty
    private List<RegistryProperty> registries;

    @NestedConfigurationProperty
    private List<ProtocolProperty> protocols;

    @NestedConfigurationProperty
    private List<ModuleProperty> modules;

    @NestedConfigurationProperty
    private List<MonitorProperty> monitors;

    @NestedConfigurationProperty
    private List<ProviderProperty> providers;

    @NestedConfigurationProperty
    private List<ConsumerProperty> consumers;

    public List<ApplicationProperty> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationProperty> applications) {
        this.applications = applications;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public List<RegistryProperty> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryProperty> registries) {
        this.registries = registries;
    }

    public List<ProtocolProperty> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolProperty> protocols) {
        this.protocols = protocols;
    }


    public List<ModuleProperty> getModules() {
        return modules;
    }

    public void setModules(List<ModuleProperty> modules) {
        this.modules = modules;
    }


    public List<MonitorProperty> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<MonitorProperty> monitors) {
        this.monitors = monitors;
    }

    public List<ProviderProperty> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderProperty> providers) {
        this.providers = providers;
    }

    public List<ConsumerProperty> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ConsumerProperty> consumers) {
        this.consumers = consumers;
    }

    @Override
    public String toString() {
        return "DubboProperty{" +
                "scanPackage='" + scanPackage + '\'' +
                ", applications=" + applications +
                ", registries=" + registries +
                ", protocols=" + protocols +
                ", modules=" + modules +
                ", monitors=" + monitors +
                ", providers=" + providers +
                ", consumers=" + consumers +
                '}';
    }
}
