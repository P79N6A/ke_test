package com.lianjia.cs.dubbo.config.springboot;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.*;
import com.lianjia.cs.dubbo.config.springboot.annotation.Reference;
import com.lianjia.cs.dubbo.config.springboot.annotation.Service;
import com.lianjia.cs.dubbo.config.springboot.entity.*;
import com.lianjia.cs.dubbo.config.springboot.extension.SpringBootExtensionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@Configuration
public class AnnotationBean extends AbstractConfig implements ApplicationContextAware, DisposableBean, InitializingBean {

    private ApplicationContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBean.class);

    private DubboProperty dubboProperty;

    private final Set<ServiceBean<?>> serviceConfigs = new ConcurrentHashSet<>();

    private SpringBootBean springBootBean;

    private final ConcurrentMap<String, ReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, ReferenceBean<?>>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        dubboProperty = applicationContext.getBean(DubboProperty.class);
        springBootBean = applicationContext.getBean(SpringBootBean.class);
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
        PropertyConfigMapper.getInstance().setDubboProperty(dubboProperty);
        export();
        refer();
    }

    private void refer() {
        Set<String> hasReferenceBeanNames = springBootBean.getRefereceBeanNames();
        if (null == hasReferenceBeanNames || hasReferenceBeanNames.size() == 0) {
            return;
        }
        for (String beanName : hasReferenceBeanNames) {
            Object hasReferenceBean = context.getBean(beanName);
            Method[] methods = hasReferenceBean.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.length() > 3 && name.startsWith("set")
                        && method.getParameterTypes().length == 1
                        && Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())) {
                    try {
                        Reference reference = method.getAnnotation(Reference.class);
                        if (reference != null) {
                            Object value = refer(reference, method.getParameterTypes()[0]);
                            if (value != null) {
                                method.invoke(hasReferenceBean, new Object[]{});
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("Failed to init remote service reference at method " + name + " in class " + hasReferenceBean.getClass().getName() + ", cause: " + e.getMessage(), e);
                    }
                }
            }
            Field[] fields = hasReferenceBean.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Reference reference = field.getAnnotation(Reference.class);
                    if (reference != null) {
                        Object value = refer(reference, field.getType());
                        if (value != null) {
                            field.set(hasReferenceBean, value);
                        }
                    }
                } catch (Throwable e) {
                    logger.error("Failed to init remote service reference at filed " + field.getName() + " in class " + hasReferenceBean.getClass().getName() + ", cause: " + e.getMessage(), e);
                }
            }
        }

    }

    private Object refer(Reference reference, Class<?> referenceClass) {
        String interfaceName;
        if (!"".equals(reference.interfaceName())) {
            interfaceName = reference.interfaceName();
        } else if (!void.class.equals(reference.interfaceClass())) {
            interfaceName = reference.interfaceClass().getName();
        } else if (referenceClass.isInterface()) {
            interfaceName = referenceClass.getName();
        } else {
            throw new IllegalStateException("The @Reference undefined interfaceClass or interfaceName, and the property type " + referenceClass.getName() + " is not a interface.");
        }
        String key = reference.group() + "/" + interfaceName + ":" + reference.version();
        ReferenceBean<?> referenceConfig = referenceConfigs.get(key);
        if (referenceConfig == null) {
            referenceConfig = new ReferenceBean<Object>(reference);
            if (void.class.equals(reference.interfaceClass())
                    && "".equals(reference.interfaceName())
                    && referenceClass.isInterface()) {
                referenceConfig.setInterface(referenceClass);
            }
            if (reference.registry() != null && reference.registry().length > 0) {
                List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
                for (String registryId : reference.registry()) {
                    if (registryId != null && registryId.length() > 0) {
                        RegistryProperty registryProperty = IdableFinder.findProperty(registryId, dubboProperty.getRegistries());
                        if (null == registryProperty) {
                            LOGGER.warn("[No Properties of {} found in Config file,please be sure that right?]", registryId);
                            continue;
                        }
                        RegistryConfig registryConfig = PropertyConfigMapper.getInstance().getRegistryConfig(registryId);
                        registryConfigs.add(registryConfig);
                    }
                }
                referenceConfig.setRegistries(registryConfigs);
            }
            if (reference.consumer() != null && reference.consumer().length() > 0) {
                List<ConsumerProperty> consumerProperties = dubboProperty.getConsumers();
                ConsumerProperty consumerProperty = IdableFinder.findProperty(reference.consumer(), consumerProperties);
                if (consumerProperty != null) {
                    referenceConfig.setConsumer(PropertyConfigMapper.getInstance().getConsumerConfig(reference.consumer()));
                }
            }
            if (reference.monitor() != null && reference.monitor().length() > 0) {
                MonitorProperty monitorProperty = IdableFinder.findProperty(reference.monitor(), dubboProperty.getMonitors());
                if (null != monitorProperty) {
                    referenceConfig.setMonitor(PropertyConfigMapper.getInstance().getMonitorConfig(reference.monitor()));
                }
            }
            if (reference.application() != null && reference.application().length() > 0) {
                ApplicationProperty applicationProperty = IdableFinder.findProperty(reference.application(), dubboProperty.getApplications());
                if (null != applicationProperty) {
                    referenceConfig.setApplication(PropertyConfigMapper.getInstance().getApplicationConfig(reference.application()));
                }
            }
            if (reference.module() != null && reference.module().length() > 0) {
                ModuleProperty moduleProperty = IdableFinder.findProperty(reference.module(), dubboProperty.getModules());
                if (null != moduleProperty) {
                    referenceConfig.setModule(PropertyConfigMapper.getInstance().getModuleConfig(reference.module()));
                }
            }
//            if (reference.consumer() != null && reference.consumer().length() > 0) {
////                referenceConfig.setConsumer((ConsumerConfig) applicationContext.getBean(reference.consumer(), ConsumerConfig.class));
//            }
            try {
                referenceConfig.afterPropertiesSet();
            } catch (RuntimeException e) {
                throw (RuntimeException) e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            referenceConfigs.putIfAbsent(key, referenceConfig);
            referenceConfig = referenceConfigs.get(key);
        }
        return referenceConfig.get();
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
            serviceConfig.setRef(bean);
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
                        RegistryProperty registryProperty = IdableFinder.findProperty(registryId, dubboProperty.getRegistries());
                        if (null == registryProperty) {
                            LOGGER.warn("[No Properties of {} found in Config file,please be sure that right?]", registryId);
                            continue;
                        }
                        registryConfigs.add(PropertyConfigMapper.getInstance().getRegistryConfig(registryId));
                    }
                }
                serviceConfig.setRegistries(registryConfigs);
            }
            if (service.provider() != null && service.provider().length() > 0) {
                ProviderProperty providerProperty = IdableFinder.findProperty(service.provider(), dubboProperty.getProviders());
                if (null == providerProperty) {
                    LOGGER.warn("[No Properties of Provider:{} Found in Config file,please make sure that right!]", service.provider());
                    continue;
                }
                ProviderConfig providerConfig = PropertyConfigMapper.getInstance().getProviderConfig(service.provider());

                serviceConfig.setProvider(providerConfig);
            }
            if (service.monitor() != null && service.monitor().length() > 0) {
                MonitorProperty monitorProperty = IdableFinder.findProperty(service.monitor(), dubboProperty.getMonitors());
                if (monitorProperty == null) {
                    LOGGER.warn("[No Properties of Monitor:{} Found in Config file,please make sure that right!]", service.monitor());
                    continue;
                }
                serviceConfig.setMonitor(PropertyConfigMapper.getInstance().getMonitorConfig(service.monitor()));
            }


            if (service.application() != null && service.application().length() > 0) {
                ApplicationProperty applicationProperty = IdableFinder.findProperty(service.application(), dubboProperty.getApplications());
                if (null == applicationProperty) {
                    LOGGER.warn("[No Properties of Application:{} Found in Config file,please make sure that right!]", service.application());
                    continue;
                }
                serviceConfig.setApplication(PropertyConfigMapper.getInstance().getApplicationConfig(service.application()));
            }
            if (service.module() != null && service.module().length() > 0) {
                ModuleProperty moduleProperty = IdableFinder.findProperty(service.module(), dubboProperty.getModules());
                if (null == moduleProperty) {
                    LOGGER.warn("[No Properties of module:{} Found in Config file,please make sure that right!]", service.module());
                    continue;
                }
                serviceConfig.setModule(PropertyConfigMapper.getInstance().getModuleConfig(service.module()));
            }

            if (service.protocol() != null && service.protocol().length > 0) {
                List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
                for (String protocolId : service.protocol()) {
                    if (protocolId != null && protocolId.length() > 0) {
                        ProtocolProperty protocolProperty = IdableFinder.findProperty(protocolId, dubboProperty.getProtocols());
                        if (null == protocolProperty) {
                            LOGGER.warn("[No Properties of Protocol:{} Found in Config file,please make sure that right!]", protocolId);
                            continue;
                        }
                        protocolConfigs.add(PropertyConfigMapper.getInstance().getProtocolConfig(protocolId));
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

}
