package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.config.*;
import com.lianjia.dubbo.config.springboot.annotation.Service;
import com.lianjia.dubbo.config.springboot.entity.*;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengtianliang on 2016/11/29.
 */
public class ServiceBean<T> extends ServiceConfig<T> implements InitializingBean, DisposableBean,BeanNameAware {
    private static final long serialVersionUID = 213195494150089726L;

    private DubboProperty dubboProperty;

    private String beanName;

    public ServiceBean() {
    }

    public ServiceBean(Service service) {
        appendAnnotation(Service.class, service);
    }

    public DubboProperty getDubboProperty() {
        return dubboProperty;
    }

    public void setDubboProperty(DubboProperty dubboProperty) {
        this.dubboProperty = dubboProperty;
    }


    @Override
    public void destroy() throws Exception {
        unexport();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getProvider() == null) {
            List<ProviderProperty> providerProperties = dubboProperty.getProviders();
            if (null == providerProperties || providerProperties.size() == 0) {
            } else if (providerProperties.size() == 1) {
                ProviderProperty providerProperty = providerProperties.get(0);
                ProviderConfig providerConfig = new ProviderConfig();
                PropertyConfigCopyer.copyProviderProperty2ProviderConfig(providerProperty, providerConfig);
                setProvider(providerConfig);
            } else {
                throw new IllegalStateException("Duplicate Provider Configs:");
            }
        }
        if (getApplication() == null
                && (getProvider() == null || getProvider().getApplication() == null)) {
            List<ApplicationProperty> applicationProperties = dubboProperty.getApplications();
            if (applicationProperties != null && applicationProperties.size() > 0) {
                ApplicationProperty applicationProperty = null;
                for (ApplicationProperty applicationProperty1 : applicationProperties) {
                    if (applicationProperty1.isDefaultApp()) {
                        if (applicationProperty != null) {
                            throw new IllegalStateException("Duplicate application configs: " + applicationProperty + " and " + applicationProperty1);
                        }
                        applicationProperty = applicationProperty1;
                    }
                }
                if (applicationProperty == null && applicationProperties.size() == 1) {
                    applicationProperty = applicationProperties.get(0);
                }

                if (applicationProperty != null) {
                    ApplicationConfig applicationConfig = new ApplicationConfig();
                    PropertyConfigCopyer.copyApplicationProperty2ApplicationConfig(applicationProperty, applicationConfig);
                    setApplication(applicationConfig);
                }
            }
        }
        if (getModule() == null
                && (getProvider() == null || getProvider().getModule() == null)) {
            List<ModuleProperty> moduleProperties = dubboProperty.getModules();
            if (null != moduleProperties && moduleProperties.size() > 0) {
                ModuleProperty moduleProperty = null;
                for (ModuleProperty moduleProperty1 : moduleProperties) {
                    if (moduleProperty1.isDefaultModule()) {
                        if (moduleProperty != null) {
                            throw new IllegalStateException("Duplicate module configs: " + moduleProperty + " and " + moduleProperty1);
                        }
                        moduleProperty = moduleProperty1;
                    }
                }
                if (moduleProperty == null && moduleProperties.size() == 1) {
                    moduleProperty = moduleProperties.get(0);
                }
                if (moduleProperty != null) {
                    ModuleConfig moduleConfig = new ModuleConfig();
                    PropertyConfigCopyer.copyModuleProperty2ModuleConfig(moduleProperty, moduleConfig);
                }
            }
        }
        if ((getRegistries() == null || getRegistries().size() == 0)
                && (getProvider() == null || getProvider().getRegistries() == null || getProvider().getRegistries().size() == 0)
                && (getApplication() == null || getApplication().getRegistries() == null || getApplication().getRegistries().size() == 0)) {
            List<RegistryProperty> registryPropertyList = dubboProperty.getRegistries();
            if (registryPropertyList != null && registryPropertyList.size() > 0) {
                List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
                for (RegistryProperty registryProperty : registryPropertyList) {
                    RegistryConfig registryConfig = new RegistryConfig();
                    PropertyConfigCopyer.copyRegistryProperty2RegistryConfig(registryProperty, registryConfig);
                    registryConfigs.add(registryConfig);
                }
                if (registryConfigs.size() > 0) {
                    setRegistries(registryConfigs);
                }
            }
        }
        if (getMonitor() == null
                && (getProvider() == null || getProvider().getMonitor() == null)
                && (getApplication() == null || getApplication().getMonitor() == null)) {
            List<MonitorProperty> monitorProperties = dubboProperty.getMonitors();
            if (null != monitorProperties && monitorProperties.size() > 0) {
                MonitorProperty monitorProperty = null;
                for (MonitorProperty monitorProperty1 : monitorProperties) {
                    if (monitorProperty1.isDefaultMonitor()) {
                        if (monitorProperty != null) {
                            throw new IllegalStateException("Duplicate monitor configs: " + monitorProperty1 + " and " + monitorProperty);
                        }
                        monitorProperty = monitorProperty1;
                    }
                }
                if (monitorProperty == null && monitorProperties.size() == 1) {
                    monitorProperty = monitorProperties.get(0);
                }
                if (monitorProperty != null) {
                    MonitorConfig monitorConfig = new MonitorConfig();
                    PropertyConfigCopyer.copyMonitoryProperty2MonitoryConfig(monitorProperty, monitorConfig);
                    setMonitor(monitorConfig);
                }
            }
        }
        if ((getProtocols() == null || getProtocols().size() == 0)
                && (getProvider() == null || getProvider().getProtocols() == null || getProvider().getProtocols().size() == 0)) {
            List<ProtocolProperty> protocolProperties = dubboProperty.getProtocols();
            if (protocolProperties != null && protocolProperties.size() > 0) {

            }
//            Map<String, ProtocolConfig> protocolConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
//            if (protocolConfigMap != null && protocolConfigMap.size() > 0) {
//                List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
//                for (ProtocolConfig config : protocolConfigMap.values()) {
//                    if (config.isDefault() == null || config.isDefault().booleanValue()) {
//                        protocolConfigs.add(config);
//                    }
//                }
//                if (protocolConfigs != null && protocolConfigs.size() > 0) {
//                    super.setProtocols(protocolConfigs);
//                }
//            }
        }
        if (getPath() == null || getPath().length() == 0) {
            if (beanName != null && beanName.length() > 0
                    && getInterface() != null && getInterface().length() > 0
                    && beanName.startsWith(getInterface())) {
                setPath(beanName);
            }
        }
//        if (!isDelay()) {
//            export();
//        }
    }


//    private boolean isDelay() {
//        Integer delay = getDelay();
//        ProviderConfig provider = getProvider();
//        if (delay == null && provider != null) {
//            delay = provider.getDelay();
//        }
//    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
