package com.lianjia.dubbo.config.springboot;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chengtianliang on 2016/11/29.
 */
@Configuration
public class SpringBootBean implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware,BeanPostProcessor {

    private static final String SCAN_PACKAGE = "dubbo.scanPackage";

    private Environment environment;

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
        String[] packages = Constants.COMMA_SPLIT_PATTERN.split(scanPackage);
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

        return bean;
    }
}
