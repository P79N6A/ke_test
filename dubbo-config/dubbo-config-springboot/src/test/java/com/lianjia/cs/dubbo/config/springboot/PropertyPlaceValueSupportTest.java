package com.lianjia.cs.dubbo.config.springboot;

import com.lianjia.cs.dubbo.config.springboot.utils.PropertyPlaceValueSupport;
import org.springframework.core.env.PropertyResolver;

public class PropertyPlaceValueSupportTest {

    public static void main(String[] args) {
        String value = new PropertyPlaceValueSupport().resolvePlaceValue("hhh${test.group:888}ddd",new MyPropertiesReslover());
        System.out.println(value);
    }

    public static class MyPropertiesReslover implements PropertyResolver{

        @Override
        public boolean containsProperty(String key) {
            return true;
        }

        @Override
        public String getProperty(String key) {
            return null;
        }

        @Override
        public String getProperty(String key, String defaultValue) {
            return key;
        }

        @Override
        public <T> T getProperty(String key, Class<T> targetType) {
            return null;
        }

        @Override
        public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
            return null;
        }

        @Override
        public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
            return null;
        }

        @Override
        public String getRequiredProperty(String key) throws IllegalStateException {
            return key;
        }

        @Override
        public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
            return null;
        }

        @Override
        public String resolvePlaceholders(String text) {
            return text;
        }

        @Override
        public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
            return text;
        }
    }
}
