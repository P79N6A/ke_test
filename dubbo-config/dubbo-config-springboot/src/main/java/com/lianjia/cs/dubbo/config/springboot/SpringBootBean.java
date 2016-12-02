package com.lianjia.cs.dubbo.config.springboot;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.lianjia.cs.dubbo.config.springboot.annotation.Reference;
import com.lianjia.cs.dubbo.config.springboot.annotation.Service;
import com.lianjia.cs.dubbo.config.springboot.entity.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@Configuration
public class SpringBootBean implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanPostProcessor, ApplicationContextAware, DisposableBean {

    private static final String SCAN_PACKAGE = "dubbo.scanPackage";

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootBean.class);

    private DubboProperty dubboProperty;

    private final ConcurrentMap<String, ReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, ReferenceBean<?>>();


    private Environment environment;

    private String[] scanPackages;

    private Set<String> refereceBeanNames = new ConcurrentHashSet<>();

    public Set<String> getRefereceBeanNames() {
        return refereceBeanNames;
    }

    private ApplicationContext context;

    @Bean
    public static DubboProperty dubboProperty() {
        return new DubboProperty();
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String scanPackage = environment.getProperty(SCAN_PACKAGE);
        if (scanPackage == null) {
            throw new BeanInitializationException(SCAN_PACKAGE + "must be set");
        }
        scan(registry, scanPackage);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private void scan(BeanDefinitionRegistry registry, String scanPackage) {
        String[] packages = scanPackages = Constants.COMMA_SPLIT_PATTERN.split(scanPackage);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, true);
        AnnotationTypeFilter serviceTypeFilter = new AnnotationTypeFilter(Service.class);
        AnnotationTypeFilter referenceFilter = new AnnotationTypeFilter(Reference.class);
        scanner.addIncludeFilter(serviceTypeFilter);
        scanner.addIncludeFilter(referenceFilter);
        scanner.scan(packages);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isMatch(bean)) {
            Class<?> clazz = bean.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Reference reference = method.getAnnotation(Reference.class);
                if (null != reference) {
                    if (null == dubboProperty)
                        dubboProperty = context.getBean(DubboProperty.class);
                    refer(bean);
                }
            }
            Field[] fields = clazz.getDeclaredFields();
            if (null != fields) {
                for (Field field : fields) {
                    Reference reference = field.getAnnotation(Reference.class);
                    if (null != reference) {
                        if (null == dubboProperty)
                            dubboProperty = context.getBean(DubboProperty.class);
                        refer(bean);
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private boolean isMatch(Object bean) {
        if (scanPackages == null || scanPackages.length == 0) return false;
        String name = bean.getClass().getName();
        for (String packageName : scanPackages) {
            if (name.startsWith(packageName)) return true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public void refer(Object hasReferenceBean) {
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
                    LOGGER.error("Failed to init remote service reference at method " + name + " in class " + hasReferenceBean.getClass().getName() + ", cause: " + e.getMessage(), e);
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
                LOGGER.error("Failed to init remote service reference at filed " + field.getName() + " in class " + hasReferenceBean.getClass().getName() + ", cause: " + e.getMessage(), e);
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
            referenceConfig.setDubboProperty(dubboProperty);
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

    @Override
    public void destroy() throws Exception {
        for (ReferenceConfig<?> referenceConfig : referenceConfigs.values()) {
            try {
                referenceConfig.destroy();
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
