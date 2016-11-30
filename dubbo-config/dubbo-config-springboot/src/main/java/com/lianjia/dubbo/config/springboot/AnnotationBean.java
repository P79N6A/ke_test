package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.*;
import com.lianjia.dubbo.config.springboot.annotation.Service;
import com.lianjia.dubbo.config.springboot.entity.*;
import com.lianjia.dubbo.config.springboot.extension.SpringBootExtensionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@Configuration
public class AnnotationBean extends AbstractConfig implements ApplicationContextAware, DisposableBean, InitializingBean {

    private ApplicationContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBean.class);

    private DubboProperty dubboProperty;

    private final Set<ServiceBean<?>> serviceConfigs = new ConcurrentHashSet<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        dubboProperty = applicationContext.getBean(DubboProperty.class);
        SpringBootExtensionFactory.addApplicationContext(context);
    }

    @Override
    public void destroy() throws Exception {
        for (ServiceConfig<?> serviceConfig : serviceConfigs) {
            try {
                serviceConfig.unexport();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringBootExtensionFactory.addApplicationContext(context);
        export();
        refer();
    }

    private void refer() {

    }

    private void export() {
        String[] serviceBeanNames = context.getBeanNamesForAnnotation(Service.class);
        if (serviceBeanNames == null || serviceBeanNames.length == 0) {
            LOGGER.warn("[No Service(Provider) to export]");
            return;
        }
        Map<String, Object> services = context.getBeansWithAnnotation(Service.class);
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            Object bean = entry.getValue();
            Service service = bean.getClass().getAnnotation(Service.class);
            ServiceBean<Object> serviceConfig = new ServiceBean<>(service);
            serviceConfig.setDubboProperty(dubboProperty);
//            serviceConfig.setApplicationContext(context);
            if (void.class.equals(service.interfaceClass())
                    && "".equals(service.interfaceName())) {
                if (bean.getClass().getInterfaces().length > 0) {
                    serviceConfig.setInterface(bean.getClass().getInterfaces()[0]);
                } else {
                    throw new IllegalStateException("Failed to export remote service class " + bean.getClass().getName() + ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
                }
            }
            if (service.registry() != null && service.registry().length > 0) {
                List<RegistryConfig> registryConfigs = new ArrayList<>();
                for (String registryId : service.registry()) {
                    if (registryId != null && registryId.length() > 0) {
                        RegistryProperty registryProperty = findPropery(registryId, dubboProperty.getRegistries());
                        if (null == registryProperty) {
                            LOGGER.warn("[No Properties of {} found in Config file,please be sure that right?]", registryId);
                            continue;
                        }
                        RegistryConfig registryConfig = new RegistryConfig();
                        PropertyConfigCopyer.copyRegistryProperty2RegistryConfig(registryProperty, registryConfig);
                        registryConfigs.add(registryConfig);
                    }
                }
                serviceConfig.setRegistries(registryConfigs);
            }
            if (service.provider() != null && service.provider().length() > 0) {
                ProviderProperty providerProperty = findPropery(service.provider(), dubboProperty.getProviders());
                if (null == providerProperty) {
                    LOGGER.warn("[No Properties of Provider:{} Found in Config file,please make sure that right!]", service.provider());
                    continue;
                }
                ProviderConfig providerConfig = new ProviderConfig();
                PropertyConfigCopyer.copyProviderProperty2ProviderConfig(providerProperty, providerConfig);
                serviceConfig.setProvider(providerConfig);
            }
            if (service.monitor() != null && service.monitor().length() > 0) {
                MonitorProperty monitorProperty = findPropery(service.monitor(), dubboProperty.getMonitors());
                if (monitorProperty == null) {
                    LOGGER.warn("[No Properties of Monitor:{} Found in Config file,please make sure that right!]", service.monitor());
                    continue;
                }
                MonitorConfig monitorConfig = new MonitorConfig();
                PropertyConfigCopyer.copyMonitoryProperty2MonitoryConfig(monitorProperty, monitorConfig);
                serviceConfig.setMonitor(monitorConfig);
            }


            if (service.application() != null && service.application().length() > 0) {
                ApplicationProperty applicationProperty = findPropery(service.application(), dubboProperty.getApplications());
                if (null == applicationProperty) {
                    LOGGER.warn("[No Properties of Application:{} Found in Config file,please make sure that right!]", service.application());
                    continue;
                }
                ApplicationConfig applicationConfig = new ApplicationConfig();
                PropertyConfigCopyer.copyApplicationProperty2ApplicationConfig(applicationProperty, applicationConfig);
                serviceConfig.setApplication(applicationConfig);
            }
            if (service.module() != null && service.module().length() > 0) {
                ModuleProperty moduleProperty = findPropery(service.module(), dubboProperty.getModules());
                if (null == moduleProperty) {
                    LOGGER.warn("[No Properties of module:{} Found in Config file,please make sure that right!]", service.module());
                    continue;
                }
                ModuleConfig moduleConfig = new ModuleConfig();
                PropertyConfigCopyer.copyModuleProperty2ModuleConfig(moduleProperty, moduleConfig);
                serviceConfig.setModule(moduleConfig);
            }

            if (service.protocol() != null && service.protocol().length > 0) {
                List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
                for (String protocolId : service.registry()) {
                    if (protocolId != null && protocolId.length() > 0) {
                        ProtocolProperty protocolProperty = findPropery(protocolId, dubboProperty.getProtocols());
                        if (null == protocolProperty) {
                            LOGGER.warn("[No Properties of Protocol:{} Found in Config file,please make sure that right!]", protocolId);
                            continue;
                        }
                        ProtocolConfig protocolConfig = new ProtocolConfig();
                        PropertyConfigCopyer.copyProtocolProperty2ProtocolConfig(protocolProperty, protocolConfig);
                        protocolConfigs.add(protocolConfig);
                    }
                }
                serviceConfig.setProtocols(protocolConfigs);
            }
            try {
                serviceConfig.afterPropertiesSet();
            } catch (RuntimeException e) {
                throw (RuntimeException) e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            serviceConfig.setRef(bean);
            serviceConfigs.add(serviceConfig);
            serviceConfig.export();
        }
    }

    private <T extends Idable> T findPropery(String id, List<T> list) {
        if (list == null) return null;
        for (T obj : list) {
            if (id.equals(obj)) {
                return obj;
            }
        }
        return null;
    }

}
