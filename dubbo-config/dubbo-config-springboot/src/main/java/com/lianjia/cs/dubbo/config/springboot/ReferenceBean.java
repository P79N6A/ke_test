/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lianjia.cs.dubbo.config.springboot;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.support.Parameter;
import com.lianjia.cs.dubbo.config.springboot.annotation.Reference;
import com.lianjia.cs.dubbo.config.springboot.entity.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceFactoryBean
 *
 * @author william.liangf
 * @export
 */
public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean, InitializingBean, DisposableBean {

    private static final long serialVersionUID = 213195494150089726L;

    private DubboProperty dubboProperty;

    public ReferenceBean() {
        super();
    }

    public ReferenceBean(Reference reference) {
        appendAnnotation(Reference.class, reference);
    }

    public DubboProperty getDubboProperty() {
        return dubboProperty;
    }

    public void setDubboProperty(DubboProperty dubboProperty) {
        this.dubboProperty = dubboProperty;
    }

    public Object getObject() throws Exception {
        return get();
    }

    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Parameter(excluded = true)
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings({"unchecked"})
    public void afterPropertiesSet() throws Exception {
        if (getConsumer() == null) {
            List<ConsumerProperty> consumerProperties = dubboProperty.getConsumers();
            if (consumerProperties != null && consumerProperties.size() > 0) {
                ConsumerProperty consumerProperty = null;
                for (ConsumerProperty consumerProperty1 : consumerProperties) {
                    if (consumerProperty1.isDefaultConsumer()) {
                        if (consumerProperty != null) {
                            throw new IllegalStateException("Duplicate consumer configs: " + consumerProperty + " and " + consumerProperty1);
                        }
                        consumerProperty = consumerProperty1;
                    }
                }
                if (consumerProperty == null && consumerProperties.size() == 1) {
                    consumerProperty = consumerProperties.get(0);
                }
                if (consumerProperty != null) {
                    setConsumer(PropertyConfigMapper.getInstance().getConsumerConfig(consumerProperty.getId()));
                }
            }
        }
        if (getApplication() == null
                && (getConsumer() == null || getConsumer().getApplication() == null)) {
            List<ApplicationProperty> applicationProperties = dubboProperty.getApplications();
            if (null != applicationProperties && applicationProperties.size() > 0) {
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
                    setApplication(PropertyConfigMapper.getInstance().getApplicationConfig(applicationProperty.getId()));
                }
            }
        }
        if (getModule() == null
                && (getConsumer() == null || getConsumer().getModule() == null)) {
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
                    setModule(PropertyConfigMapper.getInstance().getModuleConfig(moduleProperty.getId()));
                }
            }
        }
        if ((getRegistries() == null || getRegistries().size() == 0)
                && (getConsumer() == null || getConsumer().getRegistries() == null || getConsumer().getRegistries().size() == 0)
                && (getApplication() == null || getApplication().getRegistries() == null || getApplication().getRegistries().size() == 0)) {
            List<RegistryProperty> registryPropertyList = dubboProperty.getRegistries();
            if (registryPropertyList != null && registryPropertyList.size() > 0) {
                List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
                for (RegistryProperty registryProperty : registryPropertyList) {
                    registryConfigs.add(PropertyConfigMapper.getInstance().getRegistryConfig(registryProperty.getId()));
                }
                if (registryConfigs.size() > 0) {
                    setRegistries(registryConfigs);
                }
            }
        }
        if (getMonitor() == null
                && (getConsumer() == null || getConsumer().getMonitor() == null)
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
                    setMonitor(PropertyConfigMapper.getInstance().getMonitorConfig(monitorProperty.getId()));
                }
            }
        }
        Boolean b = isInit();
        if (b == null && getConsumer() != null) {
            b = getConsumer().isInit();
        }
        if (b != null && b.booleanValue()) {
            getObject();
        }
    }

}