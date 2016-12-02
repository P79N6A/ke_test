package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.registry.Registry;
import com.lianjia.dubbo.config.springboot.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lianjia.dubbo.config.springboot.IdableFinder.findProperty;

/**
 * Created by chengtianliang on 2016/12/2.
 */
public class PropertyConfigMapper {
    private PropertyConfigMapper() {
    }

    private Map<String, ProviderConfig> providerConfigMap = new ConcurrentHashMap<>();

    private Map<String, ConsumerConfig> consumerConfigMap = new ConcurrentHashMap<>();

    private Map<String, ProtocolConfig> protocolConfigMap = new ConcurrentHashMap<>();

    private Map<String, RegistryConfig> registryConfigMap = new ConcurrentHashMap<>();

    private Map<String, ModuleConfig> moduleConfigMap = new ConcurrentHashMap<>();

    private Map<String, ApplicationConfig> applicationConfigMap = new ConcurrentHashMap<>();

    private Map<String, MonitorConfig> monitorConfigMap = new ConcurrentHashMap<>();

    private AtomicBoolean hasInited = new AtomicBoolean(false);

    private volatile DubboProperty dubboProperty;

    public void setDubboProperty(DubboProperty dubboProperty) {
        if (this.dubboProperty != dubboProperty) {
            reset();
            hasInited.set(false);
        }
        this.dubboProperty = dubboProperty;
        init();
    }

    private static class Hoder {
        private static PropertyConfigMapper instance = new PropertyConfigMapper();
    }

    public static PropertyConfigMapper getInstance() {
        return Hoder.instance;
    }

    private void init() {
        if (dubboProperty == null) {
            hasInited.set(false);
            reset();
            return;
        }
        if (hasInited.getAndSet(false)) {
            buildConfigs();
            afterBuildConfigs();
        }
    }

    private void buildConfigs() {
        List<ProviderProperty> providerProperties = dubboProperty.getProviders();
        if (null != providerProperties) {
            for (ProviderProperty providerProperty : providerProperties) {
                ProviderConfig providerConfig = new ProviderConfig();
                PropertyConfigCopyer.copyProviderProperty2ProviderConfig(providerProperty, providerConfig);
                providerConfigMap.put(providerConfig.getId(), providerConfig);
            }
        }
        List<ConsumerProperty> consumerProperties = dubboProperty.getConsumers();
        if (null != consumerProperties) {
            for (ConsumerProperty consumerProperty : consumerProperties) {
                ConsumerConfig consumerConfig = new ConsumerConfig();
                PropertyConfigCopyer.copyConsumeProperty2ConsumeConfig(consumerProperty, consumerConfig);
                consumerConfigMap.put(consumerConfig.getId(), consumerConfig);
            }
        }

        List<ProtocolProperty> protocolProperties = dubboProperty.getProtocols();
        if (null != protocolProperties) {
            for (ProtocolProperty protocolProperty : protocolProperties) {
                ProtocolConfig protocolConfig = new ProtocolConfig();
                PropertyConfigCopyer.copyProtocolProperty2ProtocolConfig(protocolProperty, protocolConfig);
                protocolConfigMap.put(protocolConfig.getId(), protocolConfig);
            }
        }

        List<RegistryProperty> registryProperties = dubboProperty.getRegistries();
        if (null != registryProperties) {
            for (RegistryProperty registryProperty : registryProperties) {
                RegistryConfig registryConfig = new RegistryConfig();
                PropertyConfigCopyer.copyRegistryProperty2RegistryConfig(registryProperty, registryConfig);
                registryConfigMap.put(registryConfig.getId(), registryConfig);
            }
        }

        List<ModuleProperty> moduleProperties = dubboProperty.getModules();
        if (null != moduleProperties) {
            for (ModuleProperty moduleProperty : moduleProperties) {
                ModuleConfig moduleConfig = new ModuleConfig();
                PropertyConfigCopyer.copyModuleProperty2ModuleConfig(moduleProperty, moduleConfig);
                moduleConfigMap.put(moduleConfig.getId(), moduleConfig);
            }
        }

        List<ApplicationProperty> applicationProperties = dubboProperty.getApplications();
        if (null != applicationProperties) {
            for (ApplicationProperty applicationProperty : applicationProperties) {
                ApplicationConfig applicationConfig = new ApplicationConfig();
                PropertyConfigCopyer.copyApplicationProperty2ApplicationConfig(applicationProperty, applicationConfig);
                applicationConfigMap.put(applicationConfig.getId(), applicationConfig);
            }
        }

        List<MonitorProperty> monitorProperties = dubboProperty.getMonitors();
        if (null != monitorProperties) {
            for (MonitorProperty monitorProperty : monitorProperties) {
                MonitorConfig monitorConfig = new MonitorConfig();
                PropertyConfigCopyer.copyMonitoryProperty2MonitoryConfig(monitorProperty, monitorConfig);
                monitorConfigMap.put(monitorConfig.getId(), monitorConfig);
            }
        }
    }

    public ProtocolConfig getProtocolConfig(String id) {
        check(id);
        return protocolConfigMap.get(id);
    }

    public RegistryConfig getRegistryConfig(String id) {
        check(id);
        return registryConfigMap.get(id);
    }

    public ApplicationConfig getApplicationConfig(String id) {
        check(id);
        return applicationConfigMap.get(id);
    }

    public ModuleConfig getModuleConfig(String id) {
        check(id);
        return moduleConfigMap.get(id);
    }

    public MonitorConfig getMonitorConfig(String id) {
        check(id);
        return monitorConfigMap.get(id);
    }

    public ConsumerConfig getConsumerConfig(String id) {
        check(id);
        return consumerConfigMap.get(id);
    }

    public ProviderConfig getProviderConfig(String id) {
        check(id);
        return providerConfigMap.get(id);
    }

    private void afterBuildConfigs() {
        for (ProviderConfig providerConfig : providerConfigMap.values()) {
            ProviderProperty providerProperty = findProperty(providerConfig.getId(), dubboProperty.getProviders());
            String protocol = providerProperty.getProtocol();
            if (null != protocol) {
                List<ProtocolConfig> protocolConfigs = new ArrayList<>();
                String[] protocols = Constants.COMMA_SEPARATOR.split(protocol);
                for (String p : protocols) {
                    ProtocolConfig protocolConfig = getProtocolConfig(p);
                    if (null == protocolConfig) {
                        throw new RuntimeException("Invalid protocol config " + protocol + " in provider config");
                    }
                    protocolConfigs.add(protocolConfig);
                }
                providerConfig.setProtocols(protocolConfigs);
            }
            String registry = providerProperty.getRegistry();
            if (null != registry) {
                if ("N/A".equalsIgnoreCase(registry)) {
                    providerConfig.setRegister(Boolean.FALSE);
                } else {
                    List<RegistryConfig> registryConfigs = new ArrayList<>();
                    String[] registries = Constants.COMMA_SEPARATOR.split(registry);
                    for (String r : registries) {
                        RegistryConfig registryConfig = getRegistryConfig(r);
                        if (registryConfig == null) {
                            throw new RuntimeException("Invalid registry Config:" + registry + " in Provider Config");
                        }
                        registryConfigs.add(registryConfig);
                    }
                    providerConfig.setRegistries(registryConfigs);
                }
            }
        }

        for (ConsumerConfig consumerConfig : consumerConfigMap.values()) {
            ConsumerProperty consumerProperty = findProperty(consumerConfig.getId(), dubboProperty.getConsumers());
            String registry = consumerProperty.getRegistry();
            if (null != registry) {
                if ("N/A".equalsIgnoreCase(registry)) {
                } else {
                    List<RegistryConfig> registryConfigs = new ArrayList<>();
                    String[] registries = Constants.COMMA_SEPARATOR.split(registry);
                    for (String r : registries) {
                        RegistryConfig registryConfig = getRegistryConfig(r);
                        if (registryConfig == null) {
                            throw new RuntimeException("Invalid registry Config:" + registry + " in Consume Config");
                        }
                        registryConfigs.add(registryConfig);
                    }
                    consumerConfig.setRegistries(registryConfigs);
                }
            }
        }
    }


    private void reset() {
        providerConfigMap.clear();
        consumerConfigMap.clear();
        protocolConfigMap.clear();
        registryConfigMap.clear();
        moduleConfigMap.clear();
        applicationConfigMap.clear();
        monitorConfigMap.clear();
    }

//    public ProviderConfig getProviderConfig(String id) {
//        check(id);
//        ProviderConfig providerConfig = providerConfigMap.get(id);
//        if (providerConfig == null) {
//            ProviderProperty providerProperty = findProperty(id, dubboProperty.getProviders());
//            if (null == providerProperty) return null;
//            providerConfig = new ProviderConfig();
//            PropertyConfigCopyer.copyProviderProperty2ProviderConfig(providerProperty, providerConfig);
//            providerConfigMap.put(id, providerConfig);
//        }
//        return providerConfig;
//    }
//
//    public

    private void check(String id) {
        if (null == id) {
            throw new RuntimeException("id is null");
        }
        if (null == dubboProperty) {
            throw new RuntimeException("dubbo property is null");
        }
    }
}
