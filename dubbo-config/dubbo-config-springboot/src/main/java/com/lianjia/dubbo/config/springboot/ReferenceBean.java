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
package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.support.Parameter;
import com.lianjia.dubbo.config.springboot.annotation.Reference;
import com.lianjia.dubbo.config.springboot.entity.ApplicationProperty;
import com.lianjia.dubbo.config.springboot.entity.ConsumerProperty;
import com.lianjia.dubbo.config.springboot.entity.DubboProperty;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    ConsumerConfig consumerConfig = new ConsumerConfig();
                    PropertyConfigCopyer.copyConsumeProperty2ConsumeConfig(consumerProperty, consumerConfig);
                    setConsumer(consumerConfig);
                }
            }
        }
        if (getApplication() == null
                && (getConsumer() == null || getConsumer().getApplication() == null)) {
            List<ApplicationProperty> applicationProperties = dubboProperty.getApplications();
            if (null != applicationProperties && applicationProperties.size() > 0) {

            }
//            Map<String, ApplicationConfig> applicationConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
//            if (applicationConfigMap != null && applicationConfigMap.size() > 0) {
//                ApplicationConfig applicationConfig = null;
//                for (ApplicationConfig config : applicationConfigMap.values()) {
//                    if (config.isDefault() == null || config.isDefault().booleanValue()) {
//                        if (applicationConfig != null) {
//                            throw new IllegalStateException("Duplicate application configs: " + applicationConfig + " and " + config);
//                        }
//                        applicationConfig = config;
//                    }
//                }
//                if (applicationConfig != null) {
//                    setApplication(applicationConfig);
//                }
//            }
        }
        if (getModule() == null
                && (getConsumer() == null || getConsumer().getModule() == null)) {
//            Map<String, ModuleConfig> moduleConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ModuleConfig.class, false, false);
//            if (moduleConfigMap != null && moduleConfigMap.size() > 0) {
//                ModuleConfig moduleConfig = null;
//                for (ModuleConfig config : moduleConfigMap.values()) {
//                    if (config.isDefault() == null || config.isDefault().booleanValue()) {
//                        if (moduleConfig != null) {
//                            throw new IllegalStateException("Duplicate module configs: " + moduleConfig + " and " + config);
//                        }
//                        moduleConfig = config;
//                    }
//                }
//                if (moduleConfig != null) {
//                    setModule(moduleConfig);
//                }
//            }
        }
        if ((getRegistries() == null || getRegistries().size() == 0)
                && (getConsumer() == null || getConsumer().getRegistries() == null || getConsumer().getRegistries().size() == 0)
                && (getApplication() == null || getApplication().getRegistries() == null || getApplication().getRegistries().size() == 0)) {
//            Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
//            if (registryConfigMap != null && registryConfigMap.size() > 0) {
//                List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
//                for (RegistryConfig config : registryConfigMap.values()) {
//                    if (config.isDefault() == null || config.isDefault().booleanValue()) {
//                        registryConfigs.add(config);
//                    }
//                }
//                if (registryConfigs != null && registryConfigs.size() > 0) {
//                    super.setRegistries(registryConfigs);
//                }
//            }
        }
        if (getMonitor() == null
                && (getConsumer() == null || getConsumer().getMonitor() == null)
                && (getApplication() == null || getApplication().getMonitor() == null)) {
//            Map<String, MonitorConfig> monitorConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, MonitorConfig.class, false, false);
//            if (monitorConfigMap != null && monitorConfigMap.size() > 0) {
//                MonitorConfig monitorConfig = null;
//                for (MonitorConfig config : monitorConfigMap.values()) {
//                    if (config.isDefault() == null || config.isDefault().booleanValue()) {
//                        if (monitorConfig != null) {
//                            throw new IllegalStateException("Duplicate monitor configs: " + monitorConfig + " and " + config);
//                        }
//                        monitorConfig = config;
//                    }
//                }
//                if (monitorConfig != null) {
//                    setMonitor(monitorConfig);
//                }
//            }
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