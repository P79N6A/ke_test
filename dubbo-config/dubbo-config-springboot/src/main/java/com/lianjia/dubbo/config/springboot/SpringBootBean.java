package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.lianjia.dubbo.config.springboot.annotation.Reference;
import com.lianjia.dubbo.config.springboot.annotation.Service;
import com.lianjia.dubbo.config.springboot.entity.DubboProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@Configuration
public class SpringBootBean implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanPostProcessor {

    private static final String SCAN_PACKAGE = "dubbo.scanPackage";

    private Environment environment;

    private String[] scanPackages;

    private Set<String> refereceBeanNames = new ConcurrentHashSet<>();

    public Set<String> getRefereceBeanNames() {
        return refereceBeanNames;
    }

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
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isMatch(bean)) {
            Class<?> clazz = bean.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Reference reference = method.getAnnotation(Reference.class);
                if (null != reference) {
                    refereceBeanNames.add(beanName);
                }
            }
            Field[] fields = clazz.getDeclaredFields();
            if (null != fields) {
                for (Field field : fields) {
                    Reference reference = field.getAnnotation(Reference.class);
                    if (null != reference) {
                        refereceBeanNames.add(beanName);
                    }
                }
            }
        }
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
}
